package mybotproject;

import java.util.List;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class MiscData {
	
	public static void help(MessageReceivedEvent event) {
		List<String> pages = MyListener.Pages;
		pages.clear();
		StringBuilder page = new StringBuilder();
		page.append("!addhealth /!removehealth /sethealth (value) - change current health \n");
		page.append("!health - see current health \n");
		page.append("!stats - shows current character sheet \n");
		page.append("!allocate (stat) (value) - allows you to spend level up points \n");
		page.append("!proxy on/off - switches between characters \n");
		page.append("!removeability \" ability name\" - removes ability from active character \n");
		page.append("!viewability \" ability name \" - views ability from active character \n");
		page.append("!renameability \"old name\" \"new name\" - changes name of active character's ability \n");
		page.append("!raceinfo \" race name\" - shows current description and bonuses \n" );
		page.append("evolutions - shows all current evolutions for current character");
		pages.add(page.toString());
		page.setLength(0);
		page.append("!additem (please see item template) - adds item to our system permanently \n");
		page.append("!searchitem item name (if longer than 1 word, use quotes) - retrieves items data \n");
		page.append("!listitems (blacksmith/general/alchemy) (locations OPTIONAL) - retrieves a list of items \n");
		page.append("!shop - retrieves shop data for current channel \n");
		page.append("!buyitems (List of numbers assocated with shop, ask moderator help if needed) - buys items in order \n");
		page.append("!setweapon/armor/enchantment \"item name\" \"item bonus\" - sets it for the ID reasons mainly \n");
		page.append("!removeitem \"item name\" - removes item from active characters bag \n");
		page.append("!viewbag - shows active characters bag \n");
		page.append("!givecoin @person cointype amount - gives that person some of your coin \n");
		page.append("!viewcoin - shows your current coin count \n");
		pages.add(page.toString());
		page.setLength(0);
		page.append("!addmonster - see template for more details \n");
		page.append("!listmonsters (locations OPTIONAL) \n");
		page.append("!searchmonster monster name (if more than 1 word, use quotes) - retrieves the monsters data \n");
		page.append("!spawnmonster location - spawns a monster of that location in your channel \n");
		page.append("!damagemonster amount - damages the current monster in this channel, if killed, will loot \n");
		page.append("!viewmonster - views current monster in this channel \n");
		page.append("!forfeit - removes current monster from channel and alerts moderators to check if successfully escaped \n");
		page.append("!r/roll - you can roll dice or stats (RECOMMENDED) ask moderator for help \n");
		page.append("!setname \" name \" - renames character \n");
		page.append("!setAlignment \"alignment \" \n");
		page.append("!set/removeAvatar (image if set) - sets or removes avatar from active character \n");
		pages.add(page.toString());
		page.setLength(0);
		page.append("!id 1 - generates an id of type 1 for active character \n");
		page.append("!id 2 - generates an id of type 2 for active character \n");
		page.append("!id 3 \" (if 3, hex code for color OPTIONAL)\" - generates an id for active character \n");
		pages.add(page.toString());
		List<Button> buttons = ButtonClickListener.nextPage(event);
		 event.getChannel().sendMessageEmbeds(MyListener.createEmbed("__HELP__",pages.get(0))).setActionRow(buttons).queue();
		
		
	}

}
