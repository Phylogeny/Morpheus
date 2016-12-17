package net.quetzi.morpheus.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.quetzi.morpheus.Morpheus;
import net.quetzi.morpheus.MorpheusRegistry;
import net.quetzi.morpheus.helpers.References;

import java.util.ArrayList;
import java.util.List;

public class CommandMorpheus extends CommandBase {

    private final List<String> aliases;

    public CommandMorpheus() {

        aliases = new ArrayList<String>();
        aliases.add("sleepvote");
    }

    @Override
    public String getCommandName() {

        return "morpheus";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {

        return References.USAGE;
    }

    @Override
    public List getCommandAliases() {

        return aliases;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] astring) throws NumberInvalidException {

        if (astring.length == 0) {
            sender.addChatMessage(new ChatComponentText(References.USAGE));
            return;
        }
        if (astring[0].equalsIgnoreCase("alert")) {
            if (Morpheus.isAlertEnabled()) {
                Morpheus.setAlertPlayers(false);
                sender.addChatMessage(new ChatComponentText(References.ALERTS_OFF));
            } else {
                Morpheus.setAlertPlayers(true);
                sender.addChatMessage(new ChatComponentText(References.ALERTS_ON));
            }
        } else if (astring[0].equalsIgnoreCase("disable")) {
            if (astring[1] != null) {
                int ageToDisable = parseInt(astring[1]);
                if (Morpheus.register.isDimRegistered(ageToDisable)) {
                    Morpheus.register.unregisterHandler(ageToDisable);
                    sender.addChatMessage(new ChatComponentText("Disabled sleep vote checks in dimension " + ageToDisable));
                } else {
                    sender.addChatMessage(new ChatComponentText("Sleep vote checks are already disabled in dimension " + ageToDisable));
                }
            } else {
                sender.addChatMessage(new ChatComponentText(References.DISABLE_USAGE));
            }
        } else if (astring[0].equalsIgnoreCase("version")) {
            sender.addChatMessage(new ChatComponentText("Morpheus version: " + References.VERSION));
        } else if (astring[0].equalsIgnoreCase("percent")) {
            if (astring[1] != null) {
                int newPercent = parseInt(astring[1]);
                if (newPercent > 0 && newPercent <= 100) {
                    Morpheus.perc = newPercent;
                    Morpheus.config.get("settings", "SleeperPerc", 50).set(newPercent);
                    Morpheus.config.save();
                    sender.addChatMessage(new ChatComponentText("Sleep vote percentage set to " + Morpheus.perc + "%"));
                } else {
                    sender.addChatMessage(new ChatComponentText("Invalid percentage value, round numbers between 0 and 100 are acceptable."));
                }
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {

        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender icommandsender, String[] args, BlockPos pos) {

    	if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, new String[]{"alerts", "disable", "percent", "version"});
        }
        if (args[0].equalsIgnoreCase("disable"))
        {
            List options = new ArrayList();
            for (Integer dimensionId : MorpheusRegistry.registry.keySet())
            {
                options.add(dimensionId.toString());
            }
            return getListOfStringsMatchingLastWord(args, options);
        }
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] astring, int i) {

        return false;
    }

    @Override
    public int getRequiredPermissionLevel() {

        return 4;
    }
}
