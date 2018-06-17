# AddonForSkillAPI
A spigot plugin that adds extra features for SkillAPI plugin

Hey folks! I've been waiting a bit to release my own addon for the SkillAPI Plugin.
It adds lots of new features that you might find useful for your server that uses
SkillAPI.

Here is a list of what my Addon does:

- Damage / Heal Holograms Indicators.
- Make all armor stands invulnerable to damage.
- Better Chat Prefix, including Level.
- A well requested feature of Block Break Event (Runs skills when breaking blocks).
- Another feature lacking on SkillAPI, protected World Guard Regions, where skills cannot be used!
- Friendly races option: Players of same class groups cant hit each other
- Fixes for skill bar errors

Everything in my plugin is toggleable, so if you don't like a feature, feel free to disable it!

Commands:
/afs reload - reloads the config
/afs changeclass <player> <class to change> <new class>
/afs setlevel <player> <class> <level>

changeclass and setlevel can be useful if player has more than one class in his/her active account.
For example if you have a class which you use for races like "Elf" and an "Elf"+"Archer" wants to change his class to "Mage" and
become an "Elf"+"Mage" you can use this command: /afs changeclass <player> Archer Mage

FAQ:

Q: - Does it work with the Free version of SAPI?
A: - Yes.

Q: - Is the code Open Source?
A: - Not at the moment, but soon!

Q: - Does it support older versions like 1.8.X?
A: - Only tested with 1.12.2 but should work with older versions
