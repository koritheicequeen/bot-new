package mybotproject;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class ItemData {
	
	String itemName;
	List<String> type;
	String bonus;
	String effects;
	List<String> locations;
	String Descriptions;
	String Rarity;
	String priceRange;
	String creator;
	List<Integer> lootChance;
	static  Map<String,LinkedHashMap<Integer,ItemData>> stores;
	// Map <StoreName, Map<ItemName, ItemData>
	public ItemData(String itemName) {
		this.itemName = itemName;
		this.type = new ArrayList<>();
		this.bonus = "";
		this.effects="";
		this.locations = new ArrayList<>();
		this.Descriptions="";
		this.Rarity="";
		this.priceRange="";
		this.lootChance = new ArrayList<>();
				;
	}
	
public static ItemData copyItem(ItemData itemData) {
	ItemData item = new ItemData(itemData.itemName);
	item.itemName= itemData.itemName;
	item.type= new ArrayList<>( itemData.type);
	item.bonus= itemData.bonus;
	item.effects= itemData.effects;
	item.locations= new ArrayList<>(itemData.locations);
	item.Descriptions= itemData.Descriptions;
	item.Rarity= itemData.Rarity;
	item.priceRange= itemData.priceRange;
	item.creator = itemData.creator;
	return item;
}
public static ItemData addItem (List<String> segments, MessageReceivedEvent event) {
	if (segments.size()<16) {
		event.getChannel().sendMessage("Please include all parts").queue();
		return null;
	}
	
	ItemData itemData = new ItemData(segments.get(1));
	if (segments.get(3).toLowerCase().contains("all")) {
		itemData.type.add("Blacksmith");
		itemData.type.add("Alchemy");
		itemData.type.add("General");
	}
	else itemData.type.add(null);
	itemData.bonus = segments.get(5);
	itemData.effects = segments.get(7);
	String[] parts = segments.get(9).split("\\s+");
	for (String part: parts) {
		itemData.locations.add(part);
	}
	itemData.Descriptions = segments.get(11);
	itemData.Rarity = segments.get(13);
	itemData.priceRange = segments.get(15);
	itemData.creator=event.getAuthor().getGlobalName();
	return itemData;
}
public static void ListUnapprovedItems( String word, MessageReceivedEvent event) {
	MyListener.pagecount = 0;
	List<String> pages = MyListener.Pages;
	pages.clear();
	Map<String, ItemData> map = null;
	if (word.toLowerCase().contains("alch")) {
		map = MyListener.AlchemistApprovalmap;
	}
	if (word.toLowerCase().contains("black")|| (word.toLowerCase().contains("smith"))) {
		map = MyListener.BlacksmithApprovalmap;
	}
	if (word.toLowerCase().contains("general")) {
		map = MyListener.GeneralApprovalmap;
	}
	if (map == null) {
		event.getChannel().sendMessage("No items matching your search").queue();
		return;
	}
	 for (Entry<String, ItemData> entry : map.entrySet()) {
		 int i = 1;
		 StringBuilder page = new StringBuilder();
	   
	        ItemData valueholder = entry.getValue();
	        page.append(word + i + "\n");
	        i++;
	        page.append("Item Name: " + valueholder.itemName + "\n"+
	        	"Type: ");
	        for (String type : valueholder.type) {
	        	page.append(type + " ");
	        }
	        page.append(
	        		 "\n" +"Bonus: " + valueholder.bonus + "\n" +
	        	"Effects: " + valueholder.effects + "\n" +
	        	"Locations: ");
	        for (String location : valueholder.locations) {
	        	page.append(location + " ");
	        }
	        page.append(
	        		"\n" +"Descriptions: " + valueholder.Descriptions +"\n" +
	        	"Rarity: " + valueholder.Rarity +"\n" +
	        	"Price Range: " + valueholder.priceRange + "\n" + "Creator: " + valueholder.creator);
	        pages.add(page.toString());
}
	 List<Button> buttons = ButtonClickListener.nextPage(event);
	 event.getChannel().sendMessageEmbeds(MyListener.createEmbed("Items that matched search",pages.get(0))).setActionRow(buttons).queue();
}

public static void verifyItem(String[] parts, Boolean check){

	Map<String, ItemData> map = null;
	Map<String, ItemData> checkedMap = null;
	Map<Map<String, ItemData>,Map<String, ItemData>> maps = new LinkedHashMap<>();
	if (parts[1].toLowerCase().equals("blacksmith")) {
	map = MyListener.BlacksmithApprovalmap;
	checkedMap = MyListener.BlacksmithCheckedmap;
	maps.put(map, checkedMap);
	}
	if (parts[1].toLowerCase().equals("general")) {
		map = MyListener.GeneralApprovalmap;
		checkedMap = MyListener.GeneralCheckedmap;
		maps.put(map, checkedMap);
	}
	if (parts[1].toLowerCase().equals("alchemy")) {
		map = MyListener.AlchemistApprovalmap;
		checkedMap = MyListener.AlchemistCheckedmap;
		maps.put(map, checkedMap);}
for (Entry<Map<String, ItemData>,Map<String, ItemData>> Maps : maps.entrySet()) {
	   map = Maps.getKey();
	   checkedMap = Maps.getValue();
	    List<Integer> items = new ArrayList<>();
	    int i = 1;
	    for (String part : parts) {
	       
	        if (i > 2) {
	            items.add(Integer.valueOf(part));
	        }
	        i++;
	    }

	    // Using an iterator to avoid ConcurrentModificationException
	    Iterator<Entry<String, ItemData>> iterator = map.entrySet().iterator();
	    int index = 1;
	    
	    while (iterator.hasNext()) {
	        Entry<String, ItemData> entry = iterator.next();

	        // Check if the current entry's position is in `items`
	        if (items.contains(index)) {
	            if (check) {
	                checkedMap.put(entry.getKey(), entry.getValue());
	            }
	            iterator.remove(); // Safely remove entry from map
	        }
	        index++;
	    }
}
	}
public static void sortItem(ItemData itemData) {
	if (itemData.type.contains("Blacksmith")) {
		MyListener.BlacksmithApprovalmap.put(itemData.itemName, itemData);
	}
	if (itemData.type.contains("Alchemy")) {
		MyListener.AlchemistApprovalmap.put(itemData.itemName, itemData);
	}
	if (itemData.type.contains("General")) {
		MyListener.GeneralApprovalmap.put(itemData.itemName, itemData);
	}

}
public static void listItems(MessageReceivedEvent event, String[] parts) {
	Map<String, ItemData> map = null;
	String word = parts[1];
	if (word.toLowerCase().contains("alch")) {
		map = MyListener.AlchemistApprovalmap;
	}
	if (word.toLowerCase().contains("black")|| (word.toLowerCase().contains("smith"))) {
		map = MyListener.BlacksmithApprovalmap;
	}
	if (word.toLowerCase().contains("general")) {
		map = MyListener.GeneralApprovalmap;
	}
	List<String> pages = MyListener.Pages;
	pages.clear();
	MyListener.pagecount=0;
	
	Boolean locationSpecific;
	List<String> Locations=new ArrayList<>();
	if (parts.length>2) {
		locationSpecific = true;
		
		for (int index = 2; index < parts.length; index++) { // Start from index 2
            Locations.add(parts[index].toLowerCase());
        }
	} else locationSpecific = false;
	
	StringBuilder page = new StringBuilder();
	 int i = 1;
	 for (Entry<String, ItemData> entry : map.entrySet()) {
		
		 ItemData itemData = entry.getValue();
		 
		 // Location check
		 if (locationSpecific) {
			 boolean foundCommon = false;
			for (String location : itemData.locations) {
				if (Locations.contains(location.toLowerCase())) {
					foundCommon = true;
					break;}}
			if (!foundCommon) {
				continue;}}
		// Location check
		 
		 page.append(itemData.itemName + " : " + itemData.bonus + " " + itemData.Rarity + "\n");
		 
		 if (i >14) {
			 pages.add(page.toString());
			 page.setLength(0);
			 i=0;
		 }
		 i++;
		 
		
	 }
	 
	 if(page.length()>0) {
	 pages.add(page.toString());}
	 List<Button> buttons = ButtonClickListener.nextPage(event);
	 event.getChannel().sendMessageEmbeds(MyListener.createEmbed("Items that matched search",pages.get(0))).setActionRow(buttons).queue();
	
}
public static String retrieveItemByName(String name) {
	ItemData valueholder = null;
	boolean approval = false;
		if (MyListener.AlchemistApprovalmap.get(name)!=null) {
		valueholder = MyListener.AlchemistCheckedmap.get(name);}
		else if (MyListener.GeneralCheckedmap.get(name)!=null) {
			valueholder = MyListener.GeneralCheckedmap.get(name);}
		else if (MyListener.BlacksmithCheckedmap.get(name)!=null) {
			valueholder = MyListener.BlacksmithCheckedmap.get(name);}
			else if (MyListener.AlchemistApprovalmap.get(name)!=null) {
				valueholder = MyListener.AlchemistApprovalmap.get(name);
				approval = true;}
				else if (MyListener.GeneralApprovalmap.get(name)!=null) {
					valueholder = MyListener.GeneralApprovalmap.get(name);
					approval = true;}
				else if (MyListener.LootApprovalmap.get(name)!=null) {
					valueholder = MyListener.LootApprovalmap.get(name);
					approval = true;}
				else if (MyListener.LootCheckedmap.get(name)!=null) {
					valueholder = MyListener.LootCheckedmap.get(name);
					approval = false;}
				else if (MyListener.BlacksmithApprovalmap.get(name)!=null) {
					valueholder = MyListener.BlacksmithApprovalmap.get(name);
					approval = true;}  else return null;
	 StringBuilder page = new StringBuilder();
     page.append("Item Name: " + valueholder.itemName);
    		 if (approval) page.append("\n" + "__Pending Approval__");
    		 page.append("\n"+
     	"Type: ");
     for (String type : valueholder.type) {
     	page.append(type + " ");
     }
     page.append(
     		 "\n" +"Bonus: " + valueholder.bonus + "\n" +
     	"Effects: " + valueholder.effects + "\n" +
     	"Locations: ");
     for (String location : valueholder.locations) {
     	page.append(location + " ");
     }
     page.append(
     		"\n" +"Descriptions: " + valueholder.Descriptions +"\n" +
     	"Rarity: " + valueholder.Rarity +"\n" +
     	"Price Range: " + valueholder.priceRange + "\n Creator: " + valueholder.creator);
return page.toString();
}
public static void refreshShops() {
	 String[] locations = {
	            "ingrasia", "easternempire", "eurazania", "holyempire", "dwargon", 
	            "falmuth", "blumund", "sarion", "fulbrosia", "dragons"
	        };
	        String[] storeTypes = { "blacksmith", "general", "alchemy" };

	        for (String location : locations) {
	            // Location/store/store, name, Itemdata
	            for (String storeType : storeTypes) {
	                stores.put(location + " "+storeType + "store", new LinkedHashMap<>());
	            }
	          
	        }
	    
	for (Entry<String, LinkedHashMap<Integer,ItemData>> entry : stores.entrySet()) {
			 String shop = entry.getKey();
			 LinkedHashMap<Integer,ItemData> itemMap = entry.getValue();
			 String[] parts = shop.split("\\s+", 3); 
			 List<ItemData> commonAvailable = new ArrayList<>();
			 List<ItemData> uncommonAvailable = new ArrayList<>();
			 List<ItemData> rareAvailable = new ArrayList<>();
			 List<ItemData> legendAvailable = new ArrayList<>();
			 List<ItemData> randomAvailable = new ArrayList<>();
			 if (shop.contains("blacksmith")) {
				 for (Entry<String, ItemData> itemlist : MyListener.BlacksmithCheckedmap.entrySet()) {
					 ItemData itemData = itemlist.getValue();
					 for (String location : itemData.locations) {
						 if (location.toLowerCase().equals(parts[0])) {
						 if (itemData.Rarity.toLowerCase().contains("uncommon")){
						 uncommonAvailable.add(itemData);
						 randomAvailable.add(itemData);
						 } else if (itemData.Rarity.toLowerCase().contains("common")){
							 commonAvailable.add(itemData);
							 randomAvailable.add(itemData);
							 } else  if (itemData.Rarity.toLowerCase().contains("rare")){
								 rareAvailable.add(itemData);
								 randomAvailable.add(itemData);
							 }else if (itemData.Rarity.toLowerCase().contains("legend")){
							 legendAvailable.add(itemData);
							 randomAvailable.add(itemData);
							 }}}}
			 }else if (shop.contains("general")) {
				 for (Entry<String, ItemData> itemlist : MyListener.GeneralCheckedmap.entrySet()) {
					 ItemData itemData = itemlist.getValue();
					 for (String location : itemData.locations) {
						 if (location.toLowerCase().equals(parts[0])) {
						 if (itemData.Rarity.toLowerCase().contains("uncommon")){
						 uncommonAvailable.add(itemData);
						 } else if (itemData.Rarity.toLowerCase().contains("common")){
							 commonAvailable.add(itemData);
							 } else  if (itemData.Rarity.toLowerCase().contains("rare")){
								 rareAvailable.add(itemData);
							 }else if (itemData.Rarity.toLowerCase().contains("legend")){
							 legendAvailable.add(itemData);
							 }}}}
			 }else if (shop.contains("alchemy")) {
				 for (Entry<String, ItemData> itemlist : MyListener.AlchemistCheckedmap.entrySet()) {
					 ItemData itemData = itemlist.getValue();
					 for (String location : itemData.locations) {
						 if (location.toLowerCase().equals(parts[0])) {
						 if (itemData.Rarity.toLowerCase().contains("uncommon")){
						 uncommonAvailable.add(itemData);
						 } else if (itemData.Rarity.toLowerCase().contains("common")){
							 commonAvailable.add(itemData);
							 } else  if (itemData.Rarity.toLowerCase().contains("rare")){
								 rareAvailable.add(itemData);
							 }else if (itemData.Rarity.toLowerCase().contains("legend")){
							 legendAvailable.add(itemData);
							 }}}}}
			for (int i = 0; i<10;i++) {
				int roll = MyListener.rollNeutral(100);
				if (roll <=50 && commonAvailable.size()>1) {
					int index = MyListener.rollNeutral(commonAvailable.size());
					ItemData itemData = commonAvailable.get(index-1);
					String[] price = itemData.priceRange.split("[-\\s]+");
					Integer finalPrice = MyListener.rollNeutral(Integer.valueOf(price[1])-Integer.valueOf(price[0]))+Integer.valueOf(price[0]);
					ItemData itemDataNew = copyItem(itemData);
					itemDataNew.priceRange = finalPrice.toString() + " "+price[2];
					itemMap.put(i+1, itemDataNew);
				}
				else if (roll <= 70&& uncommonAvailable.size()>0) {
					int index = MyListener.rollNeutral(uncommonAvailable.size());
					ItemData itemData = uncommonAvailable.get(index-1);
					String[] price = itemData.priceRange.split("[-\\s]+");
					Integer finalPrice = MyListener.rollNeutral(Integer.valueOf(price[1])-Integer.valueOf(price[0]))+Integer.valueOf(price[0]);
					ItemData itemDataNew = copyItem(itemData);
					itemDataNew.priceRange = finalPrice.toString() + " "+price[2];
					itemMap.put(i+1, itemDataNew);
				}
				else if (roll <= 95&& rareAvailable.size()>0) {
					int index = MyListener.rollNeutral(rareAvailable.size());
					ItemData itemData = rareAvailable.get(index-1);
					String[] price = itemData.priceRange.split("[-\\s]+");
					Integer finalPrice = MyListener.rollNeutral(Integer.valueOf(price[1])-Integer.valueOf(price[0]))+Integer.valueOf(price[0]);
					ItemData itemDataNew = copyItem(itemData);
					itemDataNew.priceRange = finalPrice.toString() + " "+price[2];
					itemMap.put(i+1, itemDataNew);
				}
				else if (roll <= 100&& legendAvailable.size()>0) {
					int index = MyListener.rollNeutral(legendAvailable.size());
					ItemData itemData = legendAvailable.get(index-1);
					String[] price = itemData.priceRange.split("[-\\s]+");
					Integer finalPrice = MyListener.rollNeutral(Integer.valueOf(price[1])-Integer.valueOf(price[0]))+Integer.valueOf(price[0]);
					ItemData itemDataNew = copyItem(itemData);
					itemDataNew.priceRange = finalPrice.toString() + " "+price[2];
					itemMap.put(i+1, itemDataNew);
				}
			}
			stores.put(shop, itemMap); 
		 }
	
}

public static Map<Integer, ItemData> retrieveShopData(MessageReceivedEvent event){
	Map<Integer, ItemData> returnValue = null;
	switch(event.getChannel().getId()) {
	case "1262174001858482217":
	case "1262174795861327903":
		returnValue = stores.get("ingrassia blacksmithstore");
		break;
	case "1262174026881700093":
		returnValue = stores.get("ingrassia generalstore");
		break;
	case "1262174831080771624":
		returnValue = stores.get("ingrassia alchemystore");
		break;
	case "1266532610956464148":
		returnValue = stores.get("easternempire blacksmithstore");
		break;
	case "1266533570311094323":
		returnValue = stores.get("easternempire alchemystore");
		break;
	case "1300436951068835912":
		returnValue = stores.get("easternempire generalstore");
		break;
	case "1300437830568382576":
		returnValue = stores.get("eurazania blacksmithstore");
		break;
	case "1266534625447120918":
		returnValue = stores.get("eurazania generalstore");
		break;
	case "1300438246014324747":
		returnValue = stores.get("eurazania alchemystore");
		break;
	case "1300438593264685117":
		returnValue = stores.get("holyempire blacksmithstore");
		break;
	case "1300439262189060198":
		returnValue = stores.get("holyempire generalstore");
		break;
	case "1266536311767044147":
	case "1266538561381863567":
		returnValue = stores.get("holyempire alchemystore");
		break;
	case "1266539088845209684":
		returnValue = stores.get("dwargon blacksmithstore");
		break;
	case "1266539211347988563":
		returnValue = stores.get("dwargon generalstore");
		break;
	case "1300439600224927828":
		returnValue = stores.get("dwargon alchemystore");
		break;
	case "1300440499257217076":
		returnValue = stores.get("falmuth blacksmithstore");
		break;
	case "1266905063905824799":
		returnValue = stores.get("falmuth generalstore");
		break;
	case "1266905201806278819":
		returnValue = stores.get("falmuth alchemystore");
		break;
	case "1266909245887217766":
		returnValue = stores.get("blumund blacksmithstore");
		break;
	case "1266908453952294912":
		returnValue = stores.get("blumund generalstore");
		break;
	case "1300441408263557172":
		returnValue = stores.get("blumund alchemystore");
		break;
	case "1300441787219050506":
		returnValue = stores.get("sarion blacksmithstore");
		break;
	case "1266912605146976286":
		returnValue = stores.get("sarion generalstore");
		break;
	case "1266912331942723606":
		returnValue = stores.get("sarion alchemystore");
		break;
	case "1267634150433751112":
		returnValue = stores.get("fulbrosia blacksmithstore");
		break;
	case "1267634195287506966":
		returnValue = stores.get("fulbrosia generalstore");
		break;
	case "1300443011448311898":
		returnValue = stores.get("fulbrosia alchemystore");
		break;
	case "1267637204688048229":
	case "1267637539494432862":
		returnValue = stores.get("dragons blacksmithstore");
		break;
	case "1267636582182158489":
		returnValue = stores.get("dragons alchemystore");
		break;
	case "1300443988758888549":
		returnValue = stores.get("dragons generalstore");
		break;
	default: event.getChannel().sendMessage("No shop in this channel").queue();
	break;
	}
	
	return returnValue;
}
public static void printShopData(MessageReceivedEvent event) {
	Map<Integer, ItemData> returnValue = retrieveShopData(event);
	StringBuilder text = new StringBuilder();
	if (returnValue != null) {
	for(int i = 1; i<11;i++) {
		ItemData itemData = returnValue.get(i);
		if (itemData != null) {
			text.append(i + ". " +itemData.itemName + " ("+itemData.Rarity + ") : " + itemData.priceRange + "\n");
		}
	}
	 event.getChannel().sendMessageEmbeds(MyListener.createEmbed("Shop Inventory",text.toString())).queue();
	}
}
public static void exchangeCurrency(UserData userData, String currencyType, Integer amountToPay) {
currencyType= currencyType.toLowerCase();
String higherTier = null;
switch (currencyType) {
case "copper": 
	higherTier = "silver";
	break;
case "silver": higherTier = "gold";
break;
case "gold": higherTier = "stellar-gold";
break;
default: return;
}
if (amountToPay <= userData.Currency.getOrDefault(currencyType, 0)) {
	return;
}
if (userData.Currency.getOrDefault(higherTier,0)<1) {
	return;
}
userData.Currency.put(higherTier, userData.Currency.get(higherTier)-1);
userData.Currency.put(currencyType, userData.Currency.get(currencyType)+100);
exchangeCurrency(userData, currencyType, amountToPay);


}
public static void buyItem(String[] textParts, UserData userData, MessageReceivedEvent event) {
	List<Integer> selection = new ArrayList<>();
	for (int i = 1; i <textParts.length; i++) {
		Integer storage = Integer.valueOf(textParts[i]);
		if (storage !=null) {
			selection.add(storage);
		}
	}
	Map<Integer, ItemData> returnValue = retrieveShopData(event);
	if (returnValue == null) {
		return;
	}
	List<Integer> lostSelections = new ArrayList<>();
	List<Integer> successful = new ArrayList<>();
	StringBuilder text = new StringBuilder();
	    
	for (Integer itemNumber : selection) {
		ItemData itemData = returnValue.get(itemNumber);
		if (itemData != null) {
			 String[] parts = itemData.priceRange.split("\\s+", 300);
			 String currency = parts[1];
			 Integer price = Integer.valueOf(parts[0]);
		Integer currentCoin = userData.Currency.get(currency);
		if (currentCoin < price) {
		exchangeCurrency(userData, currency, price);
		currentCoin = userData.Currency.get(currency);
		}
		if (currentCoin < price){
		lostSelections.add(itemNumber);
		} else { 
		userData.Currency.put(currency, currentCoin-price);
		userData.Bag.put(itemData.itemName, itemData.Descriptions);
		successful.add(itemNumber);
		returnValue.remove(itemNumber);
		}
			
		} else lostSelections.add(itemNumber);
	}
	
	if (!successful.isEmpty()) {
		text.append("Thank you for your purchase of : ");
		for (Integer Int: successful) {
			text.append(Int + " ");
		}
	}
		if (!lostSelections.isEmpty()) {
			text.append("\n Unfortunately, it seems your purchase of : ");
			for (Integer Int: lostSelections) {
				text.append(Int + " ");
			}	
	}
	event.getChannel().sendMessage(text).queue();
}

}
