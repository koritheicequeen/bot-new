package mybotproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;


public class MonsterData {
	
	HashMap<String, Integer> stats;
	HashMap<String, String> information;
	HashMap<String, ItemData> Loot;
	List<String> locations;
	String name;
	static HashMap<String, MonsterData> CurrentMonsters = new LinkedHashMap<>();
	static HashMap<String, MonsterData> AllMonsters = new LinkedHashMap<>();
	static HashMap<String, MonsterData> ApprovalMonsters = new LinkedHashMap<>();
	static HashMap<String,HashMap<String, MonsterData>> monsterMap = new LinkedHashMap<>();
	
	
	public MonsterData(String name) {
		this.name=name;
		this.stats= new LinkedHashMap<>();
		this.stats.put("stats", 0);
		this.stats.put("health", 0);
		this.stats.put("maxHp", 0);
		this.stats.put("vitality", 0);
		this.stats.put("strength", 0);
		this.stats.put("endurance", 0);
		this.stats.put("dexterity", 0);
		this.stats.put("agility", 0);
		this.stats.put("willpower", 0);
		this.stats.put("magic", 0);
		this.Loot= new LinkedHashMap<>();
		this.information = new LinkedHashMap<>();
		this.information.put("description", null);
		this.information.put("effects", null);
		this.locations=new ArrayList<>();
		//Dont forget Creator in informationMap
		
	}
	public static MonsterData copyMonsterData(MonsterData monsterData, String floor) {
		MonsterData monsterDataNew = new MonsterData(monsterData.name);
		 monsterDataNew.stats = new LinkedHashMap<>(monsterData.stats); // Create a new map with copied entries
		    monsterDataNew.Loot = new LinkedHashMap<>(monsterData.Loot); // Create a new map with copied entries
		    monsterDataNew.information = new LinkedHashMap<>(monsterData.information); // Create a new map with copied entries
		    monsterDataNew.locations.add(floor);
		return monsterDataNew;
	}
	
	public static void addMonster(String content, List<String> segments, MessageReceivedEvent event) {
		MonsterData monsterData = new MonsterData(segments.get(1));
		monsterData.stats.put("vitality", Integer.valueOf(pullstats("Vitality", content)));
		monsterData.stats.put("health", monsterData.stats.get("vitality")*5);
		monsterData.stats.put("maxHp", monsterData.stats.get("vitality")*5);
		monsterData.stats.put("strength", Integer.valueOf(pullstats("Strength", content)));
		monsterData.stats.put("endurance", Integer.valueOf(pullstats("Endurance", content)));
		monsterData.stats.put("dexterity", Integer.valueOf(pullstats("Dexterity", content)));
		monsterData.stats.put("agility", Integer.valueOf(pullstats("Agility", content)));
		monsterData.stats.put("willpower", Integer.valueOf(pullstats("Willpower", content)));
		monsterData.stats.put("magic", Integer.valueOf(pullstats("Magic", content)));
		Integer stats = monsterData.stats.get("vitality")+	monsterData.stats.get("strength")+monsterData.stats.get("endurance")+	monsterData.stats.get("dexterity")+monsterData.stats.get("agility")+monsterData.stats.get("willpower")+monsterData.stats.get("magic");
		monsterData.stats.put("stats", stats);
		 String[] parts = segments.get(3).split("\\s+");
		 List<String> locations = new ArrayList<>();
		 for (String part : parts) {
			 part = part.replace(",", "");
			 if (part.contains("-")) {
				 String[] numbers = part.split("\\-", 2);
				 for(Integer i =Integer.valueOf(numbers[0]); i<=Integer.valueOf(numbers[1]);i++) {
					 locations.add(i.toString()); } 
			 }else locations.add(part);
		 }
		 monsterData.locations=locations;
		 monsterData.information.put("description", segments.get(5));
		 monsterData.information.put("effects", segments.get(7));
		 monsterData.information.put("creator", event.getAuthor().getName());
		 String[] loots = content.split("Loot Name");
		 for (String loot : loots) {
			 List<String> lootparts = MyListener.splitByQuotes(loot);
			 if (lootparts.size() >13) {
			 ItemData itemData = new ItemData(lootparts.get(1));
			 itemData.bonus = lootparts.get(3);
			 itemData.effects = lootparts.get(5);
			 itemData.Descriptions = lootparts.get(7);
			 itemData.Rarity = lootparts.get(9);
			 itemData.priceRange = lootparts.get(11);
			 
			 if (lootparts.get(4).contains("-")) {
				 String[] numbers = lootparts.get(4).split("\\-", 2);
				 for(Integer i =Integer.valueOf(numbers[0]); i<=Integer.valueOf(numbers[1]);i++) {
					 itemData.lootChance.add(i);}}
			 MyListener.LootApprovalmap.put(lootparts.get(1), itemData);
			 }
		 }
		 ApprovalMonsters.put(segments.get(1), monsterData);
		 event.getChannel().sendMessage("Monster has been added").queue();
	}
	
	public static void ListAllMonsters(MessageReceivedEvent event, String[] parts) {
		 Map<String, MonsterData> map = AllMonsters;
		MyListener.Pages.clear();
		MyListener.pagecount=0;
		List<String> locationList = new ArrayList<>();
		for (int i = 1; i <parts.length;i++) {
			locationList.add(parts[i]);
		}
		for (String locations : locationList) {
		StringBuilder page = new StringBuilder();
		 int i = 1;
		 for (Entry<String, MonsterData> entry : map.entrySet()) {
			
			 MonsterData monsterData = entry.getValue();
			 if (locations!= null) {
				 if (monsterData.locations.contains(locations)) {
					 page.append(monsterData.name + " : Stat Total : " + monsterData.stats.get("stats"));}
				 } else  page.append(monsterData.name + " : Stat Total : " + monsterData.stats.get("stats"));
			 
			 if (i >14) {
				 MyListener.Pages.add(page.toString());
				 page.setLength(0);
				 i=0;
			 }
			 i++;
		 
			
		 }
		 if(page.length()>0) {
			 MyListener.Pages.add(page.toString());}
		}
		 List<Button> buttons = ButtonClickListener.nextPage(event);
		 event.getChannel().sendMessageEmbeds(MyListener.createEmbed("monsters that matched search",MyListener.Pages.get(0))).setActionRow(buttons).queue();
		
	}
	public static String retrieveMonsterByName(String name) {
		MonsterData valueholder = null;
		boolean approval = false;
		if (AllMonsters.get(name)!=null) {
						valueholder = AllMonsters.get(name);
						approval = false;}
					else if (ApprovalMonsters.get(name)!=null) {
						valueholder = ApprovalMonsters.get(name);
						approval = true;}  else return null;
		 StringBuilder page = new StringBuilder();
	        page.append("Monster Name: " + valueholder.name + "\n");
	        if (approval) {
	        	page.append("__Under approval__ \n");
	        }
	        page.append(
	        		 "\n" +"Description: " + valueholder.information.get("description") + "\n" +"\n" +
	        	"Effect(s): " + valueholder.information.get("effects") + "\n" +
	        	"Locations: ");
	        for (String location : valueholder.locations) {
	        	page.append(location + " ");
	        }
	        for (Entry<String, ItemData> items : valueholder.Loot.entrySet()) {
	        	  page.append("\n" + items.getKey() + " : " + items.getValue().bonus + " " + items.getValue().effects+ " " + items.getValue().lootChance + " " + items.getValue().priceRange + "\n");
	        }
	      page.append( valueholder.information.get("creator"));
	return page.toString();
	}
	public static void ListUnapprovedMonsters(MessageReceivedEvent event) {
		List<String> pages = MyListener.Pages;
		pages.clear();
		MyListener.pagecount = 0;
		 for (Entry<String, MonsterData> entry : ApprovalMonsters.entrySet()) {
			 int i = 1;
			 StringBuilder page = new StringBuilder();
		   
		        MonsterData valueholder = entry.getValue();
		        page.append("Monster: " + i + "\n");
		        i++;
		        page.append("Monster Name: " + valueholder.name + "\n");
		        page.append(
		        		 "\n" +"Description: " + valueholder.information.get("description") + "\n" +"\n" +
		        	"Effect(s): " + valueholder.information.get("effects") + "\n" +
		        	"Locations: ");
		        
		        for (String location : valueholder.locations) {
		        	page.append(location + " ");
		        }
		        for (Entry<String, Integer> stat : valueholder.stats.entrySet()) {
		        	String statname = stat.getKey();
		        	Integer statvalue = stat.getValue();
		        	page.append("\n"+statname + ": " +statvalue);
		        }
		        for (Entry<String, ItemData> items : valueholder.Loot.entrySet()) {
		        	  page.append("\n" + items.getKey() + " : " + items.getValue().bonus + " " + items.getValue().effects+ " " + items.getValue().lootChance + " " + items.getValue().priceRange + "\n");
		        }
		      page.append( valueholder.information.get("creator"));
		        pages.add(page.toString());
	}
		 List<Button> buttons = ButtonClickListener.nextPage(event);
		 event.getChannel().sendMessageEmbeds(MyListener.createEmbed("Monsters that matched search",pages.get(0))).setActionRow(buttons).queue();
	}
	
	public static void verifyMonster(String[] parts, Boolean check){
		 Map<String, MonsterData> map = ApprovalMonsters;
		 Map<String, MonsterData> checkedMap = AllMonsters;
	    // Parse integer values from parts
	    List<Integer> monsters = new ArrayList<>();
	    int i = 1;
	    for (String part : parts) {
	       
	        if (i > 2) {
	            monsters.add(Integer.valueOf(part));
	        }
	        i++;
	    }

	    // Using an iterator to avoid ConcurrentModificationException
	    Iterator<Entry<String, MonsterData>> iterator = map.entrySet().iterator();
	    int index = 1;
	    
	    while (iterator.hasNext()) {
	        Entry<String, MonsterData> entry = iterator.next();

	        // Check if the current entry's position is in `items`
	        if (monsters.contains(index)) {
	            if (check) {
	                checkedMap.put(entry.getKey(), entry.getValue());
	            }
	            iterator.remove(); // Safely remove entry from map
	        }
	        index++;
	    }
	}
	public static void damageMonster(MessageReceivedEvent event, Integer Damage, UserData userData) {
		MonsterData monsterData = CurrentMonsters.get(event.getChannel().getId());
		if (monsterData==null) {
			event.getChannel().sendMessage("No monster is in this channel").queue();
			return;
		}
		Integer currentHealth = monsterData.stats.get("health");
		currentHealth = currentHealth - Damage;
		monsterData.stats.put("health", currentHealth);
		if (currentHealth>0) {
			event.getChannel().sendMessage("You have dealt " +Damage + " to the "+ monsterData.name + "\n" + currentHealth+"/"+monsterData.stats.get("maxHp") + " remaining").queue();
			return;
		} else {
			Integer roll = MyListener.rollNeutral(100);
			ItemData itemData= null;
			for (Entry<String, ItemData> entry : monsterData.Loot.entrySet()) {
				if (entry.getValue().lootChance.contains(roll)) {
				userData.Bag.put(entry.getValue().itemName, entry.getValue().Descriptions);
				itemData = entry.getValue();
				}
			}
			StringBuilder text = new StringBuilder();
			text.append("Congratulations, you have slain the " + monsterData.name);
			if (itemData != null) {
				text.append("\n You have obtained a " + itemData.itemName + "!");
			}
			event.getChannel().sendMessage(text).queue();
			 Iterator<Entry<String, MonsterData>> iterator = CurrentMonsters.entrySet().iterator();
			    int index = 1;
			    
			    while (iterator.hasNext()) {
			        Entry<String, MonsterData> entry = iterator.next();
				if (monsterData == entry.getValue()) {
					iterator.remove();
				}
			}
			
		}
		
	}
	public static void viewCurrentMonster(MessageReceivedEvent event) {
		MonsterData valueholder = CurrentMonsters.get(event.getChannel().getId());
		StringBuilder page = new StringBuilder();
		 page.append("Monster Name: " + valueholder.name + "\n");
	        page.append(
	        		 "\n" +"Description: " + valueholder.information.get("description") + "\n" +"\n" +
	        	"Effect(s): " + valueholder.information.get("effects") + "\n" +
	        	"Locations: ");
	        
	        for (String location : valueholder.locations) {
	        	page.append(location + " ");
	        }
	        for (Entry<String, Integer> stat : valueholder.stats.entrySet()) {
	        	String statname = stat.getKey();
	        	Integer statvalue = stat.getValue();
	        	page.append("\n"+statname + ": " +statvalue);
	        }
	        page.append( valueholder.information.get("creator"));
	        event.getChannel().sendMessage(page).queue();
	}
	
	public static void spawnMonster(MessageReceivedEvent event, String floorNumber) {
		TextChannel channel = (TextChannel) event.getChannel();
		Category category = channel.getParentCategory();
		String channelId = channel.getId();
		List<MonsterData> applicableMonsters = new ArrayList<>();
		Boolean successful = null;
		if (event.getChannel().getType().isThread()) {
			 ThreadChannel threadChannel = (ThreadChannel) event.getChannel();
			 String parentChannelId = threadChannel.getParentChannel().getId();
			 channelId = event.getChannel().getId();
			 if (parentChannelId.equals("1262484219909640234")) {
		   successful = true;
		   }
		}else if (category != null) {
			
		    String categoryId = category.getId();
		   		    if (categoryId.equals("1262559451542458460")) {
		      successful = true;
		    }
		}else if (channelId.equals("1262484219909640234")) {
			successful = true;
		}
		if (successful==null) {
		channel.sendMessage("Monsters aren't allowed here!").queue();
		return;
		}
		
if (CurrentMonsters.get(channelId)!=null && CurrentMonsters.get(floorNumber)!=null) {
			
			event.getChannel().sendMessage("A monster already dwells here. Please defeat it first or create a thread").queue();
			return;
		}
for (Entry<String, MonsterData> entry : AllMonsters.entrySet()) {
	if (floorNumber.equals(entry.getKey())) {
		applicableMonsters.add(entry.getValue());
	}}
	if (!applicableMonsters.isEmpty()) {
		Integer random = MyListener.rollNeutral(applicableMonsters.size());
		MonsterData mon = applicableMonsters.get(random-1);
		MonsterData monsterData = copyMonsterData(mon, floorNumber);
		CurrentMonsters.put(channelId, monsterData);
		CurrentMonsters.put(floorNumber, monsterData);
	}


	}
	
	public static void forfeitMonster(MessageReceivedEvent event) {
		MonsterData monsterData = CurrentMonsters.get(event.getChannel().getId());
	
		 Iterator<Entry<String, MonsterData>> iterator = CurrentMonsters.entrySet().iterator();
		    
		  boolean forfeit = false;  
		    while (iterator.hasNext()) {
		        Entry<String, MonsterData> entry = iterator.next();
			if (monsterData == entry.getValue()) {
				iterator.remove();
				forfeit = true;
			}
	}
		if (forfeit)    event.getChannel().sendMessage("You have forfeited the monster ||@Moderator||");
		else event.getChannel().sendMessage("there is no monster here");
}
	
	public static String pullstats (String statname, String message) {
	    Pattern pattern = Pattern.compile(statname +":\\s*(\\d+)");
	    Matcher matcher = pattern.matcher(message);
	    String statValue = "0";
	    
	    if (matcher.find()) {
	        statValue = matcher.group(1);
	     }        
	    return statValue;
	}
}
