package me.alexanderritter02.blcdisabler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.MalformedJsonException;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

import static net.minecraft.server.command.CommandManager.literal;

public class BadlionClientDisabler implements ModInitializer {

    public static final Gson GSON_NON_PRETTY = new GsonBuilder().enableComplexMapKeySerialization().disableHtmlEscaping().create();
    public static final Gson GSON_PRETTY = new GsonBuilder().enableComplexMapKeySerialization().disableHtmlEscaping().setPrettyPrinting().create();

    public static final String FOLDER = "config";
    public static final String CONFIG_FILE = "blcdisabler.json";
    public static final Identifier DISABLE_BADLION = new Identifier("badlion", "mods");

    public static final Logger logger = LogManager.getLogger();
    public static Config config;

    @Override
    public void onInitialize() {
        File disablerConfig = new File(FOLDER, CONFIG_FILE);
        config = loadConfig(disablerConfig);

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            register(dispatcher);
        });

        logger.info("Badlion Client Disabler loaded, waiting for players to join...");
    }

    private Config loadConfig(File file) {
        try {
            Reader reader = new BufferedReader(new FileReader(file));
            Config config = GSON_NON_PRETTY.fromJson(reader, Config.class);
            if(config != null) return config;
            throw new MalformedJsonException("");
        } catch(Exception e) {
            logger.info("No Config Found: Saving default...");
            Config config = new Config();
            this.saveConf(config, file);
            return config;
        }
    }

    private void saveConf(Config config, File file) {
        try {
            file.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(file);
            GSON_PRETTY.toJson(config, writer);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            logger.error("Config could not be saved.");
            e.printStackTrace();
        }
    }

    public static void sendDisablePacket(ServerPlayerEntity player) {
        byte[] message = GSON_NON_PRETTY.toJson(config.getModsDisallowed()).getBytes();
        PacketByteBuf buf = new PacketByteBuf(Unpooled.wrappedBuffer(message));
        CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(DISABLE_BADLION, buf);
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, packet);
    }

    private LiteralCommandNode register(CommandDispatcher<ServerCommandSource> dispatcher) {
        return dispatcher.register(literal("blcdisabler")
                .requires(source -> source.hasPermissionLevel(3))
                .then(literal("reload")
                    .executes(ctx -> reloadConfig(ctx))
                    )
            );
    }

    public int reloadConfig(CommandContext<ServerCommandSource> ctx) {
        config = loadConfig(new File(FOLDER, CONFIG_FILE));

        MinecraftServer server = ctx.getSource().getMinecraftServer();
        for(ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            sendDisablePacket(player);
        }

        ctx.getSource().sendFeedback(new LiteralText("Successfully reloaded disabled Badlion modules!"), false);
        return 1;
    }

}