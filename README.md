# BadlionClientDisabler
 A server-side fabric mod to allow server owners to **disable** features and mods in the [Badlion client](https://client.badlion.net/).

> By default all mods on the Badlion Client are not restricted and a user can enable any of the mods at anytime. By using this mod you remove the ability for a user to activate certain mods or features of mods while they are playing on your server. The user will gain control over the ability to use these mods/features again when they leave your server.


:warning: A full list of modules and submodules that can be disabled is provided [here](https://github.com/AlexanderRitter02/BadlionClientDisabler/wiki/Modules-to-disable).


## Installation

1. Download the latest release corresponding to your Minecraft version from the [official CurseForge page](https://www.curseforge.com/minecraft/mc-mods/badlionclientdisabler/files) or the [Github release page](https://github.com/AlexanderRitter02/BadlionClientDisabler/releases).
2. Place the downloaded jar into your `mods` directory on your Fabric server.
3. Start the server and a default config will created at `config/blcdisabler.json`
4. Edit the config as you see fit and run the command `/blcdisabler reload` ingame or in the console (see below for more information).

### Example Config

To make a config place the information into `config/blcdisabler.json` in JSON format.  
:green_book: [**List of all features that can be used in the config.**](https://github.com/AlexanderRitter02/BadlionClientDisabler/wiki/Modules-to-disable)  
If you have any JSON errors it will *re-create* the default config. A quick and easy way to test that your JSON config is valid is to use this tool: https://jsonformatter.curiousconcept.com/


This example config will fully disable the waypoints and minimap mods. It will not disable togglesneak mod, but it will block the player from using the inventorySneak and flySpeed features of the togglesneak and togglesprint mods.

```json
{
    "modsDisallowed": {
        "Waypoints": {
            "disabled": true
        },
        "MiniMap": {
            "disabled": true
        },
        "ToggleSneak": {
            "disabled": false,
            "extra_data": {
                "inventorySneak": {
                    "disabled": true
                }
            }
        },
        "ToggleSprint": {
            "disabled": false,
            "extra_data": {
                "flySpeed": {
                    "disabled": true
                }
            }
        }
    }
}
```

## Compiling
1. Clone this repository and navigate into the directory
2. Launch a build with Gradle using `gradlew build` (Windows CMD) or `./gradlew build` (macOS/Linux/Powershell)
3. After Gradle has finished building everything, you can find the resulting artifacts in `build/libs`.
