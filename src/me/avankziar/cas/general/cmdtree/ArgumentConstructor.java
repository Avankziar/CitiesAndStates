package me.avankziar.cas.general.cmdtree;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ArgumentConstructor extends BaseConstructor
{
	public CommandSuggest.Type suggestType;
    public int minArgsConstructor;
    public int maxArgsConstructor;
    public int minArgsTablist;
    public int maxArgsTablist;
    public ArrayList<ArgumentConstructor> subargument; //aka bei /scc global add, das add, also nachfogende argument
    public LinkedHashMap<Integer, ArrayList<String>> tabList;

    public ArgumentConstructor(
    		CommandSuggest.Type cst,
    		String path, int position, int minArgs, int maxArgs, boolean canConsoleAccess, boolean putUpCmdPermToValueEntrySystem,
    		LinkedHashMap<Integer, ArrayList<String>> tablistAddingOtherValue,
    		ArgumentConstructor...argumentConstructors)
    {
    	super(
    			getYamlHandling().getCommandString(path+".Argument"),
    			path,
    			getYamlHandling().getCommandString(path+".Permission"),
    			getYamlHandling().getCommandString(path+".Suggestion"),
    			getYamlHandling().getCommandString(path+".CommandString"),
    			getYamlHandling().getCommandString(path+".HelpInfo"),
    			canConsoleAccess,
    			putUpCmdPermToValueEntrySystem);
    	this.suggestType = cst;
        this.minArgsConstructor = minArgs;
        this.maxArgsConstructor = maxArgs;
        this.minArgsTablist = minArgs;
        this.maxArgsTablist = maxArgs;
        this.subargument = new ArrayList<>();
        this.tabList = new LinkedHashMap<>();
        if(tablistAddingOtherValue != null)
        {
        	this.tabList = tablistAddingOtherValue;
        }
        ArrayList<String> tl = tabList.get(position);
        if(tl == null)
        {
        	tl = new ArrayList<>();
        }
        for(ArgumentConstructor ac : argumentConstructors)
        {
        	CommandSuggest.set(ac.suggestType, ac);
        	getHelpList().add(ac);
        	subargument.add(ac);
        	tl.add(ac.getName());
        }
        if(tabList.containsKey(position))
        {
        	tabList.replace(position, tl);
        } else
        {
        	tabList.put(position, tl);
        }
    }
    
    public ArgumentConstructor getSubArgument(String argument)
    {
    	ArgumentConstructor argc = null;
    	for(ArgumentConstructor ac : subargument)
    	{
    		if(ac.getName().equals(argument))
    		{
    			argc = ac;
    			break;
    		}
    	}
    	return argc;
    }
}