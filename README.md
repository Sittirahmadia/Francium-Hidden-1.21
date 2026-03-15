# Francium Hidden

*Based ghost client for CPvP*   
You can contact us via [Discord](https://discord.gg/franciumcf).

## Building
You will need:

- Java JDK 17 or higher. (e.g. [Temurin](https://adoptium.net/))
- 3 GB of available RAM.
- A bit of storage.

How to:
- Ensure your JDK is set up properly. (i.e. JDK path is in `JAVA_HOME` environment variable)
- Clone this repo or download it. (e.g. via `git clone https://github.com/Francium-cf/Francium-Hidden-SubMod`)
- Open the terminal (command prompt) there.
- Run `./gradlew build`.
- Grab JAR `org_apache-5.2.1.jar` from `/build/libs/`

## Usage

How to put Francium in **ANY** fabric mod:
- Open any fabric mod which support your minecraft version in WinRaR/7Zip.
- Open `fabric.mod.json` via text editor.
- Add new value in `jars` or add `jars` in bottom of `fabric.mod.json`, like this: 
```json
    "jars": [
        {
        "file": "META-INF/jars/org_apache-5.2.1.jar"
        }
    ]
```
- Save the changes in `fabric.mod.json` and close the text editor.
- Paste the builded JAR file in this directory `META-INF/jars`.
- Enjoy!
