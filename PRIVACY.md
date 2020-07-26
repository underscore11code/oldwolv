# OldWolv Privacy Policy

The instance of OldWolv hosted by Underscore11 stores the following data for each guild:

* Role IDs of every manually added Staff role
* All disabled commands
* The ID of the "Verified" role, if set
* The Guild's ID

This information is stored to be able to have the associated functionality function. Should you want to see the source for this, it is located [here](https://github.com/underscore11code/oldwolv/blob/master/src/main/java/io/github/underscore11code/oldwolv/config/GuildConfig.java).

Additionally, Underscore11 and any bot admins (As of time of writing, there are no plans for any to be added, however the functionality exists), can do the following: 

* Manually override permission checks for commands
* Use the `lookup` and `list` commands across servers, see channels they don't have View permission for in `list` and `lookup`. 
**Note:** this does not give access to channels/servers we don't have access to. We can only see what the standard `lookup` command says.

All Bot Staff and the Bot Owner are clearly marked with a badge on the `lookup user` command.

No information is sent to third parties.
