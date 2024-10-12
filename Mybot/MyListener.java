package mybotproject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import org.jetbrains.annotations.NotNull;


public class MyListener extends ListenerAdapter {

    private static final String DATA_FILE = "userData.json";
    public Map<String, UserData> userDataMap;
    private static final int levelUpPoints = 5; // Example level up points
    public String fileName = "image.png";
   
    public MyListener() {
    
        loadUserData();
    }
@Override
    public void onMessageReceived(MessageReceivedEvent event) {
	String userId = event.getAuthor().getId();
    userDataMap.putIfAbsent(userId, new UserData(userId));
    UserData userData = userDataMap.get(userId);
    if (userData.Proxy==true) {
    	userId = userData.ProxyUserID;
    }
    StringBuilder notes = new StringBuilder();
    String notesSend = "";
    /*
     */
        if (event.getAuthor().isBot()) return;

        String content = event.getMessage().getContentRaw();
        Attachment attachment = event.getMessage().getAttachments().stream().findFirst().orElse(null);
        List<String> segments = splitByQuotes(content);
        if (content.isBlank() || !content.startsWith("!")) return;

        String[] parts = content.split("\\s+", 50); // Split by whitespace, limit to 3 parts
        String command = parts[0];
        command = command.toLowerCase();
        
        Member targetMember = parts.length > 1 ? extractMemberFromMention(parts[1], event) : null;
        
        List<String> modifiers = new ArrayList<>();
        List<String> dice = new ArrayList<>();
       
        
        
        int amount = parts.length == 3 ? parseValue(parts[2]) : 0;

        if (isModerator(event.getMember()) || command.equals("!stats") || command.equals("!allocate" )
        		|| command.equals("!health") ||(command.equals("!addhealth")&& parts.length<3)|| 
        		(command.equals("!removehealth")&& parts.length<3)||
        		(command.equals("!sethealth")&& parts.length<3)||
        		command.equals("!help")|| command.equals("!helproll") ||
        		(command.equals("!viewability") && !content.contains("@")) ||
        		command.equals("!setnote") || (command.equals("!setName") && !content.contains("@"))
        		|| (command.equals("!removeability")  && !content.contains("@"))|| (command.equals("!renameability") &&!content.contains("@"))
        		|| (command.equals("!id")&&!content.contains("@"))
        		|| command.equals("!raceinfo")
        		|| command.equals("!setweapon")
        		|| command.equals("!setarmor")
        		|| command.equals("!setenchantment")
        		|| command.equals("!setavatar")
        		|| command.equals("!removeavatar")
        		||(command.equals("!storeitem")  && !content.contains("@"))
        		|| (command.equals("!removeitem")  && !content.contains("@"))
        		||(command.equals("!viewcoin")  && !content.contains("@"))
        		||(command.equals("!alignment")  && !content.contains("@"))
        		||command.equals("!givecoin")
        		||(command.equals("!viewbag")  && !content.contains("@"))
        		||(command.equals("!proxy")  && !content.contains("@"))
        		||(command.equals("!evolutions"))
        				
        		
        		
        				
        				
        		
        		
        				
        				) {
        	if (command.equals("!helpmoderator")) {
        		if (parts.length>1 && parts[1].equals("1")) {
        		event.getChannel().sendMessageEmbeds(createEmbed("Bot Commands Moderator edition",
        				 "\n" + "**Please keep in mind, any () are purely to represent missing values**"+ "\n" + "\n" +
        				"!add(Stat) @member (value) - Allows Moderators to add stats to whoever they mention" +"\n" + "\n" +
        		"!remove(Stat) @member (value) - Allows Moderators to remove stats to whoever they mention" +"\n" +"\n"
        						+"!set(Stat) @member (value) - Allows Moderators to set the stat of whoever they mention to the value" +"\n" +"\n"
        						+"!viewStats @member - Allows moderators to view the stats of whoever they mention" +"\n" +"\n"
        						+"!setStats @member followed by their stat sheet - Allows moderators to update the stats of whomever they mention" +"\n"+"\n"
        						
        						+"!addHealth @member (value) - Allows Moderators to add Health up to MaxHP" +"\n" + "\n"
        						+"!removeHealth @member (value) - Allows Moderators to remove Health up to -(MaxHP)" +"\n" + "\n"
        						+"!setHealth @member (value) - Allows Moderators to set Health up to MaxHP and -(MaxHP)" +"\n" + "\n"
        						+"!setName @member \"(Character name)\" - Allows Moderators to Change the  character name of whomever was mentioned"+ "\n" + "\n"
        						+ "!addAbility @member \"(Ability name)\""+ " \"(Ability text)\" - adds an ability to the mentioned's list with the following text"  + "\n" + "\n"
        						+ "!removeAbility @member \"(Ability name)\" - Allows a Moderator to Remove the listed ability from the mentioned's list" +"\n" + "\n"
        						+ "!viewAbility @member \"(Ability name)\" - Allows a Moderator to show the listed ability from the specific mentioend's list"+ "\n"+ "\n"
        						+ "!renameAbility @member \"(old Ability name)\" \"(new ability name)\" - Renames an ability of the mentioned"+ "\n"+ "\n"
        						+"!helpModerator 2 - Shows more commands only Moderators can use"
        				
        						)).queue();} else {
        							event.getChannel().sendMessageEmbeds(createEmbed("Bot Commands Moderator edition",""+
        							 "!addEXP @member (amount) - adds EXP (DO NOT USE ON YOURSELF)"+ "\n"+ "\n"
        							 + "!removeEXP @member (amount) - removes EXP from whoever is mentioned"+ "\n"+ "\n"
        							 + "!setEXP @member (amount) - Sets their total EXP to whatever amount is specified"+ "\n"+ "\n"
        							 + "!id @member (1,2,3) - Generates the ID of the respective type"+ "\n"+ "\n"
        							 + "!storeItem @member \"(Item name)\" \"(Item description)\" - Adds the specied item to their bag"+ "\n"+ "\n"
        							 +"!setCoin @member (coin type) (amount)- sets their coin of that amount"+ "\n" + "\n"
        							 +"!viewCoin @member - Shows their current account"+ "\n" + "\n"
        							 +"!removeCoin @member (coin type) (amount)- Removes that amount of coins of that type"+ "\n" + "\n"
        							 +"!alignment @member \"(Alignment)\" - Sets their alignment"+ "\n" + "\n"
        							 +"!removeItem @member \"(Item name)\" - Removes the item forcibly from their bag"+ "\n" + "\n"
        							 +"!viewBag @member - Shows their inventory"+ "\n" + "\n"
        							 +"!proxy @member (on/off) - turns on their proxy to add stuff to their proxy"+ "\n" + "\n"
        							 +"!setRace @member \"(Race Name)\" - sets their race (BE ACCURATE PLEASE)"+ "\n" + "\n"
        							 +"!setName @member \"(character name)\" - puts that as their new name!"+ "\n" + "\n"
        				)).queue();
        						}
        	}else if (command.equals("!help")){
        		if (parts.length<2) {
        		event.getChannel().sendMessageEmbeds(createEmbed("Bot Commands",  
        		"\n" + "**Please keep in mind, any () are purely to represent missing values**"+ "\n" + "\n" 
        				+"!stats - Allows anyone to see their own stats" +"\n"+"\n"
        				+"!Health - Shows current health of the user" +"\n"+"\n"
        				+"!removeHealth (value) - Allows you to remove Health to yourself up to -(MaxHP)" +"\n" + "\n"
        				+"!setHealth (value) - Allows you to set your Health up to MaxHP and -(MaxHP)" +"\n" + "\n"
        				+"!allocate (Stat) (Value) - Allows you to spend your level up points to add stats"  +"\n" + "\n"
        				+"!viewAbility \"(Ability Name)\" - Allows you to bring up the ability text with that name" +"\n" + "\n"
        				+"!removeAbility \"(Ability Name)\" - Allows you to remove the listed ability" +"\n"+ "\n"
        				+"!renameAbility \"(old Ability name)\" \"(new ability name)\" - Allows you to rename an ability"+ "\n"+ "\n"
        				+"!setNote \"(Note text)\" - Allows you to store anything as a note"+"\n" + "\n"
        				+"!note - fetches whatever note you have stored "+ "\n" + "\n"
        				+"!setName \"(Character name)\" - Changes your character name to whatever you provided"+ "\n" + "\n"
        				+"!help 2 - See more commands!" +"\n" + "\n"
        				+"!helpRoll - See more about rolling!" +"\n" + "\n"
        				+"!helpModerator - Shows commands only Moderators can use"
        		)).queue();}
        		else if (parts[1].equals("1")) {
            		event.getChannel().sendMessageEmbeds(createEmbed("Bot Commands",  
                    		"\n" + "**Please keep in mind, any () are purely to represent missing values**"+ "\n" + "\n" 
                    				+"!stats - Allows anyone to see their own stats" +"\n"+"\n"
                    				+"!Health - Shows current health of the user" +"\n"+"\n"
                    				+"!removeHealth (value) - Allows you to remove Health to yourself up to -(MaxHP)" +"\n" + "\n"
                    				+"!setHealth (value) - Allows you to set your Health up to MaxHP and -(MaxHP)" +"\n" + "\n"
                    				+"!allocate (Stat) (Value) - Allows you to spend your level up points to add stats"  +"\n" + "\n"
                    				+"!viewAbility \"(Ability Name)\" - Allows you to bring up the ability text with that name" +"\n" + "\n"
                    				+"!removeAbility \"(Ability Name)\" - Allows you to remove the listed ability" +"\n"+ "\n"
                    				+"!renameAbility \"(old Ability name)\" \"(new ability name)\" - Allows you to rename an ability"+ "\n"+ "\n"
                    				+"!setNote \"(Note text)\" - Allows you to store anything as a note"+"\n" + "\n"
                    				+"!note - fetches whatever note you have stored "+ "\n" + "\n"
                    				+"!setName \"(Character name)\" - Changes your character name to whatever you provided"+ "\n" + "\n"
                    				+"!help 2 - See more commands!" +"\n" + "\n"
                    				+"!helpRoll - See more about rolling!" +"\n" + "\n"
                    				+"!helpModerator - Shows commands only Moderators can use"
                    		)).queue();}
        		else
        		event.getChannel().sendMessageEmbeds(createEmbed("Bot Commands",  
        		"\n" + "**Please keep in mind, any () are purely to represent missing values**"+ "\n" + "\n" 
        				+"!proxy yes/no - Changes your character to your second slot"+ "\n" + "\n"
        				
        				+"!setWeapon \"(Weapon name)\" \"(Bonus)\" - Equips the weapon"+ "\n" + "\n"
        				+"!setArmor \"(Armor name)\" \"(Bonus)\" - Equips the Armor"+ "\n" + "\n"
        				+"!setEnchantment \"(Enchantment Name)\" \"(Enchantment Text)\" - Equips the Enchantment"+ "\n" + "\n"
        				+"!viewEquipment - Views equipped items"+ "\n" + "\n"
        				+"!storeItem \"(Item name)\" \"(Description)\" - Puts the item in your bag"+ "\n" + "\n"
        				+"!viewBag - Views the items put in your bag the item in your bag"+ "\n" + "\n"
        				+"!id (1, 2, or 3) - Sends an ID card of that type for your character"+ "\n" + "\n"
        				+"!raceInfo \"(name of race)\" - Brings up the racial bonus for that race"+ "\n" + "\n"
        				+"!setAvatar - Sets your character image to whatever is attached"+ "\n" + "\n"
        				+"!removeAvatar - Removes your saved avatar"+ "\n" + "\n"
        				+"!viewCoin - Shows your current account"+ "\n" + "\n"       				
        				+"!giveCoin @member (coin type) (amount) - gives that amount of coins of that type to whoever you mention"+ "\n" + "\n"
        				+"!alignment \"(alignment)\" - Sets your alignment. This can be changed at any time"+ "\n" + "\n"
        				+"!removeItem \"(item)\" - removes the item from your bag"+ "\n" + "\n"
        				)).queue();
        	}else if(command.equals("!helproll")) {
        		event.getChannel().sendMessageEmbeds(createEmbed("Dice Commands ",
       				 "\n" + "**Please keep in mind, any () are purely to represent missing values**"+ "\n" + "\n" +
        		"__!roll or !r followed by a space are the main commands to roll the bot__" +"\n" + "\n"
        		+"(number of dice)d(stat) is how you roll a dice." +"\n" + "\n"
        		+"```if you do (number of dice)d(stat) + (number of dice)d(stat) it will add the results together" +"\n" + "\n"
        		+"if you do (number of dice)d(stat)/(value) itll divide the stat before rolling" +"\n" + "\n"
        		+"if you do (number of dice)d(stat)*(value) itll multiply the stat before rolling```" +"\n" + "\n"
        		+"if you do (number of dice)d(stat)+(Value) itll add the stat after all rolls```" +"\n" + "\n"
        				)).queue();}
        	else if (command.equals("!addstrength")) {
            	if (parts.length < 3) {
                    event.getChannel().sendMessage("Please use the command in the format !add<stat> @mention <amount>").queue();
            	} else handleStatCommand("strength", targetMember, amount, event);
            } else if (command.equals("!adddexterity")) {
            	if (parts.length < 3) {
            		 event.getChannel().sendMessage("Please use the command in the format !add<stat> @mention <amount>").queue();
            	} else
                handleStatCommand("dexterity", targetMember, amount, event);
            } else if (command.equals("!health")) {
            	if (targetMember == null) {
            		targetMember = event.getMember();
            	}
            	showVit(targetMember, event);
        }else if (command.equals("!addagility")) {
            	if (parts.length < 3) {
            		 event.getChannel().sendMessage("Please use the command in the format !add<stat> @mention <amount>").queue();
            	} else
                handleStatCommand("agility", targetMember, amount, event);
            } else if (command.equals("!addendurance")) {
            	if (parts.length < 3) {
            		 event.getChannel().sendMessage("Please use the command in the format !add<stat> @mention <amount>").queue();
            	} else
                handleStatCommand("endurance", targetMember, amount, event);
            } else if (command.equals("!addwillpower")) {
            	if (parts.length < 3) {
            		 event.getChannel().sendMessage("Please use the command in the format !add<stat> @mention <amount>").queue();
            	} else
                handleStatCommand("willpower", targetMember, amount, event);
            } else if (command.equals("!addmagic")) {
            	if (parts.length < 3) {
            		 event.getChannel().sendMessage("Please use the command in the format !add<stat> @mention <amount>").queue();
            	} else
                handleStatCommand("magic", targetMember, amount, event);
            } else if (command.equals("!addexp")) {
            	if (parts.length < 3) {
            		 event.getChannel().sendMessage("Please use the command in the format !add<stat> @mention <amount>").queue();
            	} else
                handleStatCommand("EXP", targetMember, amount, event);
            } else if (command.equals("!addvitality")) {
            	if (parts.length < 3) {
            		 event.getChannel().sendMessage("Please use the command in the format !add<stat> @mention <amount>").queue();
            	} else
                handleStatCommand("vitality", targetMember, amount, event);
            }else if (command.equals("!addhealth")) {
            	if (parts.length < 3) {
            		handleStatCommand("HP",event.getMember(), Integer.valueOf(parts[1]), event);
            	} else
                handleStatCommand("HP", targetMember, amount, event);
            }
            else if (command.equals("!removestrength")) {
            	if (parts.length < 3) {
            		 event.getChannel().sendMessage("Please use the command in the format !remove<stat> @mention <amount>").queue();
            	} else
                handleRemoveStatCommand("strength", targetMember, amount, event);
            }else if (command.equals("!removevitality")) {
            	if (parts.length < 3) {
            		event.getChannel().sendMessage("Please use the command in the format !remove<stat> @mention <amount>").queue();
            	} else
                handleRemoveStatCommand("vitality", targetMember, amount, event);
            }else if (command.equals("!removehealth")) {
            	if (parts.length < 3) {
            		 handleRemoveStatCommand("HP", event.getMember() , Integer.valueOf(parts[1]), event);
            	} else
                handleRemoveStatCommand("HP", targetMember, amount, event);
            }
            else if (command.equals("!removedexterity")) {
            	if (parts.length < 3) {
            		event.getChannel().sendMessage("Please use the command in the format !remove<stat> @mention <amount>").queue();
            	} else
                handleRemoveStatCommand("dexterity", targetMember, amount, event);
            } else if (command.equals("!removeagility")) {
            	if (parts.length < 3) {
            		event.getChannel().sendMessage("Please use the command in the format !remove<stat> @mention <amount>").queue();
            	} else
                handleRemoveStatCommand("agility", targetMember, amount, event);
            } else if (command.equals("!removeendurance")) {
            	if (parts.length < 3) {
            		event.getChannel().sendMessage("Please use the command in the format !remove<stat> @mention <amount>").queue();
            	} else
                handleRemoveStatCommand("endurance", targetMember, amount, event);
            } else if (command.equals("!removewillpower")) {
            	if (parts.length < 3) {
            		event.getChannel().sendMessage("Please use the command in the format !remove<stat> @mention <amount>").queue();
            	} else
                handleRemoveStatCommand("willpower", targetMember, amount, event);
            } else if (command.equals("!removemagic")) {
            	if (parts.length < 3) {
            		event.getChannel().sendMessage("Please use the command in the format !remove<stat> @mention <amount>").queue();
            	} else
                handleRemoveStatCommand("magic", targetMember, amount, event);
            } else if (command.equals("!removeexp")) {
            	if (parts.length < 3) {
            		event.getChannel().sendMessage("Please use the command in the format !remove<stat> @mention <amount>").queue();
            	} else
                handleRemoveStatCommand("EXP", targetMember, amount, event);
            } else if (command.equals("!setstrength")) {
            	if (parts.length < 3) {
            		event.getChannel().sendMessage("Please use the command in the format !set<stat> @mention <amount>").queue();
            	} else
                handleSetStatCommand("strength", targetMember, amount, event);
            }else if (command.equals("!setvitality")) {
            	if (parts.length < 3) {
            		event.getChannel().sendMessage("Please use the command in the format !set<stat> @mention <amount>").queue();
            	} else
                    handleSetStatCommand("vitality", targetMember, amount, event);
            
            }else if (command.equals("!sethealth")) {
            	if (parts.length < 3) {
            		handleSetStatCommand("HP", event.getMember(), Integer.valueOf(parts[1]), event);
            	} else		
                handleSetStatCommand("HP", targetMember, amount, event);
               
        } else if (command.equals("!setdexterity")) {
        	if (parts.length < 3) {
        		event.getChannel().sendMessage("Please use the command in the format !set<stat> @mention <amount>").queue();
        	} else
                handleSetStatCommand("dexterity", targetMember, amount, event);
            } else if (command.equals("!setagility")) {
            	if (parts.length < 3) {
            		event.getChannel().sendMessage("Please use the command in the format !set<stat> @mention <amount>").queue();
            	} else
                handleSetStatCommand("agility", targetMember, amount, event);
            } else if (command.equals("!setendurance")) {
            	if (parts.length < 3) {
            		event.getChannel().sendMessage("Please use the command in the format !set<stat> @mention <amount>").queue();
            	} else
                handleSetStatCommand("endurance", targetMember, amount, event);
            } else if (command.equals("!setwillpower")) {
            	if (parts.length < 3) {
            		event.getChannel().sendMessage("Please use the command in the format !set<stat> @mention <amount>").queue();
            	} else
                handleSetStatCommand("willpower", targetMember, amount, event);
            } else if (command.equals("!setmagic")) {
            	if (parts.length < 3) {
            		event.getChannel().sendMessage("Please use the command in the format !set<stat> @mention <amount>").queue();
            	} else
                handleSetStatCommand("magic", targetMember, amount, event);
            } else if (command.equals("!setexp")) {
            	if (parts.length < 3) {
            		event.getChannel().sendMessage("Please use the command in the format !set<stat> @mention <amount>").queue();
            	} else
                handleSetStatCommand("EXP", targetMember, amount, event);
            } else if (command.equals("!viewstats")) {
                handleViewStats(targetMember, event);
            } else if (command.equals("!stats")) {
                handleViewStats(event.getMember(), event);
                
            } else if (command.equals("!graburl")) {
            returnURL(event);
            } else if (command.equals("!id")) {
            	String check="";
            	if (parts.length<2) {
            		check = "3";
            	}
            	if (parts.length>1&& !parts[1].contains("@")) {
            		check = parts[1];
            	}
            	if (parts.length>2 && parts[1].contains("@")) {
            		check = parts[2];
            	}
            	if (check.equals("")) {
            		check="3";
            	}
            		String color = "";
            	if (targetMember== null) {
            		targetMember = event.getMember();
            	}if (segments.size()>1) {
            		
            			color = segments.get(1);
            			
            		} if (check.equals("1")) {
            	IDCard1(event, targetMember);
            		}
            		else if (check.equals("2")) {
            			IDCard2(event, targetMember);
            		}
            	 else
            	IDCardCrafting(targetMember, event, color);
        } else if (command.equals("!addability")) {
        	if (segments.size() <3) {
        		event.getChannel().sendMessage("Please include both the name and description inside of separate quotations").queue();
        		return;
        	}
            	addAbility(targetMember, segments.get(1), segments.get(2),event);
            }else if (command.equals("!removeability")) {
            	if (targetMember == null) {
            		targetMember = event.getMember();
            	}
            	if (segments.size() <3) {
            		event.getChannel().sendMessage("Please include both the name and description inside of separate quotations").queue();
            		return;
            	}
            	removeAbility(segments.get(1), targetMember, event);
            }else if (command.equals("!viewability")){
            	if (targetMember == null) {
            		targetMember = event.getMember();
            	}
            	if (segments.size() <2) {
            		event.getChannel().sendMessage("Please include the name inside quotations").queue();
            		return;
            	}
            	viewAbility(segments.get(1), targetMember, event);
            } else if (command.equals("!raceinfo")){
            	if (segments.size()<2) {
            		event.getChannel().sendMessage("Please put the race name in quotes, and make sure to capitalize the name").queue();
            		return;
            	}
            	String raceInfo = segments.get(1);
            	if (UserData.Humanoid.containsKey(raceInfo)) {
            	 event.getChannel().sendMessageEmbeds(createEmbed("Race Info "+ raceInfo, UserData.Humanoid.get(raceInfo))).queue();}
            	else if (UserData.DemiHuman.containsKey(raceInfo)) {
               	 event.getChannel().sendMessageEmbeds(createEmbed("Race Info "+ raceInfo, UserData.DemiHuman.get(raceInfo))).queue();}
            	else if (UserData.Monster.containsKey(raceInfo)) {
               	 event.getChannel().sendMessageEmbeds(createEmbed("Race Info "+ raceInfo, UserData.Monster.get(raceInfo))).queue();}
            	else {
            		event.getChannel().sendMessage("Please capitalize the first letters of the race name").queue();
            	}
            	
            	
        }else if (command.equals("!setweapon")){
        	String Name = "";
        	String Bonus = "";
        	if (segments.size()>1) {
        		Name = segments.get(1);
        		Bonus = segments.get(2);
        	}
            	EquipWeapon(event.getMember(), event, Name, Bonus);
            }else if (command.equals("!setenchantment")){
            	String Name = "";
            	String Bonus = "";
            	if (segments.size()>1) {
            		Name = segments.get(1);
            		Bonus = segments.get(2);
            	}
            	EquipEnchanted(event.getMember(), event, Name, Bonus);
            }else if (command.equals("!setarmor")){
            	String Name = "";
            	String Bonus = "";
            	if (segments.size()>1) {
            		Name = segments.get(1);
            		Bonus = segments.get(2);
            	}
            	EquipArmor(event.getMember(), event, Name, Bonus);
            }else if (command.equals("!viewequipment")){
    viewEquipment(event.getMember(), event);
    
        } else if (command.equals("!setavatar")) {
        	imageRetrieval(event);
        }else if (command.equals("!removeavatar")) {
        		userData.Avatar="";
        }else if (command.equals("!storeitem")) {
        	if (targetMember == null) {
        		targetMember = event.getMember();
        	}
        	addBag(segments.get(1), segments.get(2), targetMember, event);
        }else if (command.equals("!setcoin")){
        coinReturn(targetMember, event, parts[2], Integer.valueOf(parts[3]));
        }
        else if (command.equals("!viewcoin")){
        	if (targetMember == null) {
        		targetMember = event.getMember();
        	}
            coinView(targetMember, event);
            }else if (command.equals("!givecoin")){
                coinGive(targetMember,event.getMember(), event, parts[2], Integer.valueOf(parts[3]));
            }else if (command.equals("!removecoin")){
                cointake(targetMember, event, parts[2], Integer.valueOf(parts[3]));
            }else if (command.equals("!alignment")){
            	if (targetMember == null) {
            		targetMember = event.getMember();
            	}
                setAlignment(targetMember, event, segments.get(1));
            }
        else if (command.equals("!removeitem")) {
        	if (targetMember == null) {
        		targetMember = event.getMember();
        	}
        	RemoveBag(segments.get(1), targetMember, event);
        }
        else if (command.equals("!viewbag")) {
        	if (targetMember == null) {
        		targetMember = event.getMember();
        	}
        	viewBag(targetMember, event, userData.Bag);
        }else if (command.equals("!proxy")){
        	if (targetMember == null) {
        		targetMember = event.getMember();
        	} if (parts.length==2) {
        	ProxyCheck(parts[1], event, targetMember);}
        	else { ProxyCheck(parts[2], event, targetMember);}
        		
        	
        }
        else if (command.equals("!setrace")) {
        	if (segments.size() <2) {
        		event.getChannel().sendMessage("Please include the race name inside of quotation marks").queue();
        		return;
        	}
            	if (parts.length == 3) {
            	AddCdata(targetMember, "Race", segments.get(1), event);}
            	 saveUserData();
        }else if (command.equals("!setnote")) {
        	notes.append("```");
        		for (int i = 1; i < parts.length; i++) {
        			notes.append(parts[i]);
            		notes.append(" ");
            		}
        		notes.append("```");
        		notesSend = notes.toString();
        		AddCdata(event.getMember(), "Note", notesSend, event);
        	  } else if (command.equals("!note")) {
    	
    	 event.getChannel().sendMessageEmbeds(createEmbed(event.getMember().getEffectiveName(),userData.getCdata("Note"))).queue();
}
        else if (command.equals("!setname")) {
        	if (targetMember == null) {
        		targetMember = event.getMember();
        	}
    	AddCdata(targetMember, "Character Name", segments.get(1), event);
} else if(command.equals("!evolutions")) {
	evolutionCheck(event.getMember(), event);
} else if (command.equals("!Health")) {
            	showVit(event.getMember(), event);
            } else if (command.equals("!renameAbility")) {
            	String oldAbility = segments.get(1);
            	String newAbility = segments.get(2);
            	if (targetMember == null) {
            		targetMember = event.getMember();
            	}
            	renameAbility(oldAbility, newAbility, targetMember, event);
        } else if (command.equals("!allocate")) {
                if (parts.length < 3) {
                    event.getChannel().sendMessage("Please use the command in the format !allocate <stat> <amount>").queue();
                    return;
                } 
                String stat = parts[1];
                int value = parseValue(parts[2]);
                
                handleAllocateStat(event.getMember(), stat, value, event);
            } else if (command.equals("!roll")|| (command.equals("!r"))) {
            	for (int i = 0; i < parts.length; i++) {
            		if(i%2!=1) {
            			modifiers.add(parts[i]);
            		}else if (i%2==1) {
            		 dice.add(parts[i]);
            		}
            	}
            	rollprocessor(modifiers, dice, event, event.getMember());
        }else if (command.equals("!setstats")) {
            	
            
            	if (parts.length ==16) {
            	String stat = parts[2];
            	String cleanedText = stat.replace(":", "");
            	stat=cleanedText;
            	String stat2 = parts[4];
            	String cleanedText2 = stat2.replace(":", "");
            	stat2=cleanedText2;
            	String stat3 = parts[6];
            	String cleanedText3 = stat3.replace(":", "");
            	stat3=cleanedText3;
            	String stat4 = parts[8];
            	String cleanedText4 = stat4.replace(":", "");
            	stat4=cleanedText4;
            	String stat5 = parts[10];
            	String cleanedText5 = stat5.replace(":", "");
            	stat5=cleanedText5;
            	String stat6 = parts[12];
            	String cleanedText6 = stat6.replace(":", "");
            	stat6=cleanedText6;
            	String stat7 = parts[14];
            	String cleanedText7 = stat7.replace(":", "");
            	stat7=cleanedText7;
            	int value = parseValue(parts[3]);
            	int value2 = parseValue(parts[5]);
            	int value3 = parseValue(parts[7]);
            	int value4 = parseValue(parts[9]);
            	int value5 = parseValue(parts[11]);
            	int value6 = parseValue(parts[13]);
            	int value7 = parseValue(parts[15]);
            	
            	addAll(stat, stat2, stat3, stat4, stat5, stat6, stat7,value,value2,value3,value4,value5,value6, value7, targetMember, event);
            	}else {
            		event.getChannel().sendMessage("Please include all 7 stats").queue();
            	}
            	}else {
            		
                event.getChannel().sendMessage("Invalid command. Please use !add, !remove, !set, !viewStats, or !stats followed by the stat and user mention.").queue();
            }
        } else {
            event.getChannel().sendMessage("You do not have permission to use this command or command does not exist").queue();
        }
    }
private void AddCdata(Member targetMember,String oldInput, String newInput, MessageReceivedEvent event) {
	  if (targetMember == null) {
          event.getChannel().sendMessage("Please mention a valid user.").queue();
          return;
      }
	  String userId = targetMember.getId();
	    UserData userData = userDataMap.get(userId);
	    if (userData.Proxy==true) {
	    	userId = userData.ProxyUserID;
	    }
	    userData = userDataMap.get(userId);
      if (userData == null || userData.Cdata == null) {
    	    // Handle the situation where userData or Cdata is null
    	    // For example, initialize Cdata explicitly if it can be null
    	    userData.Cdata = new HashMap<>();
    	}
      userData.Cdata.put(oldInput, newInput);
      event.getChannel().sendMessageEmbeds(createEmbed("Your " + oldInput + " was changed", targetMember.getEffectiveName() + "'s " + oldInput+ " is now " + "\n"+ userData.getCdata(oldInput))).queue();
}
private void addAll(
	    String string, String string2, String string3, String string4, String string5, String string6, String string7,
	    int value, int value2, int value3, int value4, int value5, int value6, int value7,
	    Member targetMember, MessageReceivedEvent event) {

	    if (targetMember == null) {
	        event.getChannel().sendMessage("Please mention a valid user.").queue();
	        return;
	    }

	    String userId = targetMember.getId();
	    UserData userData = userDataMap.get(userId);
	    if (userData.Proxy) {
	    	userId = userData.ProxyUserID;
	    }
	    userData = userDataMap.get(userId);
	    userDataMap.putIfAbsent(userId, new UserData(userId));
	    userData = userDataMap.get(userId);
	    if (userData.stats == null) {
 		    userData.stats = new HashMap<>();}

	    // Convert stat names to lowercase
	    String stat1 = string.toLowerCase();
	    String stat2 = string2.toLowerCase();
	    String stat3 = string3.toLowerCase();
	    String stat4 = string4.toLowerCase();
	    String stat5 = string5.toLowerCase();
	    String stat6 = string6.toLowerCase();
	    String stat7 = string7.toLowerCase();

	    // Set stats
	    userData.setStat(stat1, value, userData);
	    userData.setStat(stat2, value2, userData);
	    userData.setStat(stat3, value3, userData);
	    userData.setStat(stat4, value4, userData);
	    userData.setStat(stat5, value5, userData);
	    userData.setStat(stat6, value6, userData);
	    userData.setStat(stat7, value7, userData);

	    // Update level and vitality
	    updateLevel(userData, targetMember, event);
	    updateVitality(userData, 0,0);

	    // Save user data
	    saveUserData();

	    // Display the updated stats
	    handleViewStats(targetMember, event);
	}


    private void handleStatCommand(String stat, Member targetMember, int amount, MessageReceivedEvent event) {
        if (targetMember == null) {
            event.getChannel().sendMessage("Please mention a valid user.").queue();
            return;
        }

        String userId = targetMember.getId();
	    UserData userData = userDataMap.get(userId);
	    if (userData.Proxy) {
	    	userId = userData.ProxyUserID;
	    }
	    userData = userDataMap.get(userId);
	    if (userData.stats == null) {
 		    userData.stats = new HashMap<>();}
	    
	    
        userData.addStat(stat, amount, userData);
        updateLevel(userData, targetMember, event);  // Update level based on new EXP
        saveUserData();
        event.getChannel().sendMessageEmbeds(createEmbed(stat.substring(0, 1).toUpperCase() + stat.substring(1) + " added for " + targetMember.getEffectiveName() + "!", "Current " + stat + ": " + userData.getStat(stat))).queue();
    }

    private void handleRemoveStatCommand(String stat, Member targetMember, int amount, MessageReceivedEvent event) {
        if (targetMember == null) {
            event.getChannel().sendMessage("Please mention a valid user.").queue();
            return;
        }

        String userId = targetMember.getId();
	    UserData userData = userDataMap.get(userId);
	    if (userData.Proxy) {
	    	userId = userData.ProxyUserID;
	    }
	    userData = userDataMap.get(userId);
	    if (userData.stats == null) {
 		    userData.stats = new HashMap<>();}
        userData.removeStat(stat, amount, userData);
        
        updateLevel(userData, targetMember, event);  // Update level based on new EXP
        saveUserData();
       
        event.getChannel().sendMessageEmbeds(createEmbed(stat.substring(0, 1).toUpperCase() + stat.substring(1) + " removed for " + targetMember.getEffectiveName() + "!", "Current " + stat + ": " + userData.getStat(stat))).queue();
    }

    private void handleSetStatCommand(String stat, Member targetMember, int amount, MessageReceivedEvent event) {
        if (targetMember == null) {
            event.getChannel().sendMessage("Please mention a valid user.").queue();
            return;
        }

        String userId = targetMember.getId();
	    UserData userData = userDataMap.get(userId);
	    if (userData.Proxy) {
	    	userId = userData.ProxyUserID;
	    }
	    userData = userDataMap.get(userId);
	    if (userData.stats == null) {
 		    userData.stats = new HashMap<>();}

        userData.setStat(stat, amount, userData);
        updateLevel(userData, targetMember, event);  // Update level based on new EXP
        saveUserData();
        event.getChannel().sendMessageEmbeds(createEmbed(stat.substring(0, 1).toUpperCase() + stat.substring(1) + " set for " + targetMember.getEffectiveName() + "!", "Current " + stat + ": " + userData.getStat(stat))).queue();
    }

    private void handleViewStats(Member targetMember, MessageReceivedEvent event) {
        if (targetMember == null) {
            event.getChannel().sendMessage("Please mention a valid user.").queue();
            return;
        }

        String userId = targetMember.getId();
	    UserData userData = userDataMap.get(userId);
	    if (userData == null ) {
    	    userData = new UserData(userId);}
    	   
	    if (userData.Proxy) {
	    	userId = userData.ProxyUserID;
	    }
	    userData = userDataMap.get(userId);
	    userDataMap.putIfAbsent(userId, new UserData(userId));
	    userData = userDataMap.get(userId);
        String name = userData.getCdata("Character Name");
        // Send message with image attachment
        if (userData.Cdata == null ) {
    	    userData.Cdata = new HashMap<>();
    	    
    	    if (name == null) {
    	    	name = targetMember.getEffectiveName();
    	    }
    	}if (name == null) {
	    	name = targetMember.getEffectiveName();
	    }
            
            if (!userData.Avatar.equals(null) && !userData.Avatar.isEmpty()) {
            	try (InputStream is = downloadImage(userData.Avatar)) {
                    if (is != null) {
                    	event.getChannel().sendMessageEmbeds(createEmbed(name+"'s Stats", getAllStats(targetMember))).queue();
                        event.getChannel().sendMessage("").addFiles(FileUpload.fromData(is, "image.png")).queue();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }} else if (userData!=null) {
        		   event.getChannel().sendMessageEmbeds(createEmbed(name+"'s Stats", getAllStats(targetMember))).queue();
        	   }}
    

    private void handleAllocateStat(Member member, String stat, int amount, MessageReceivedEvent event) {
        if (member == null) {
            event.getChannel().sendMessage("An error occurred. Please try again.").queue();
            return;
        }
        
        String userId = member.getId();
	    UserData userData = userDataMap.get(userId);
	    if (userData.Proxy) {
	    	userId = userData.ProxyUserID;
	    }
	    userData = userDataMap.get(userId);
         String stats = stat.toLowerCase();
        int availablePoints = userData.getStat("Remaining Points");
        if (amount > availablePoints) {
            event.getChannel().sendMessage("You do not have enough Remaining Points. You have " + availablePoints + " points available.").queue();
            return;}
if (amount <0) {
	amount = 0;
}
           
                   
        

        // Debug to ensure correct stat before modification

     
        userData.addStat(stats, amount, userData);
       
        userData.addStat("Remaining Points", -amount, userData);
        
        saveUserData();
        event.getChannel().sendMessageEmbeds(createEmbed("Points Allocated!", "Current " + stats + ": " + userData.getStat(stats) + "\nRemaining Points left: " + userData.getStat("Remaining Points"))).queue();}
    

    
    private void showVit (Member member, MessageReceivedEvent event) {
    	String userId = member.getId();
	    UserData userData = userDataMap.get(userId);
	    if (userData.Proxy) {
	    	userId = userData.ProxyUserID;
	    }
	    userData = userDataMap.get(userId);
    	 if (userData != null) {
    		 updateVitality(userData,0,0);
event.getChannel().sendMessageEmbeds(createEmbed(member.getEffectiveName() + "'s Current HP", "Health(HP): " + userData.getStat("HP")+"/"+userData.getStat("Health"))).queue();
    	    
    	 }
    }
    private void updateLevel(UserData userData, Member member, MessageReceivedEvent event) {
        int exp = userData.getStat("EXP");
        int newLevel = calculateLevel(exp);
        int currentLevel = userData.getStat("Level");
        if (newLevel>5 && userData.Cdata.get("Race").equals("Insectar")) {
        	userData.setStat("Level", 6, userData);
        userData.setStat("EXP", 21, userData);
        checkEvolution(member, event, currentLevel, 6);
        userData.addStat("Remaining Points", (6 - currentLevel) * levelUpPoints, userData);
        return;
        }
        if (newLevel > currentLevel) {
            
            userData.addStat("Remaining Points", (newLevel - currentLevel) * levelUpPoints, userData);
            checkEvolution(member, event, currentLevel, newLevel);
        }
       
        
         userData.setStat("Level", newLevel, userData);
         saveUserData();
    }

    public static int calculateLevel(int exp) {
        return (int) Math.floor((-1 + Math.sqrt(1 + 8 * exp)) / 2);
        
    }
    static void updateVitality(UserData userData, int CurrentVit, int newVit) {
        int vit = userData.getStat("vitality");
        int health;
        int health2;
        if (newVit!=CurrentVit) {
        health = calculateHealth(vit);
        health2 = calculateHealth(vit);
        userData.setStat("HP", health, userData);
        userData.setStat("Health", health2, userData);
        
        } 
        }
    public static int calculateHealth(int vit) {
        return (int) vit*5;
    }

    private Member extractMemberFromMention(String mention, MessageReceivedEvent event) {
        String id = mention.replaceAll("[^0-9]", "");
        if (id == "") {
        	return event.getMember();
        }
        return event.getGuild().getMemberById(id);
    }

    private boolean isModerator(Member member) {
        return member.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("Moderator"));
    }

    private int parseValue(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private MessageEmbed createEmbed(String title, String description) {
        return new EmbedBuilder()
                .setTitle(title)
                .setDescription(description)
                .setColor(Color.BLUE)
                .build();
    }

    private String getAllStats(Member member) {
    	
    	String userId = member.getId();
	    UserData userData = userDataMap.get(userId);
	    if (userData.Proxy) {
	    	userId = userData.ProxyUserID;
	    }
	    userData = userDataMap.get(userId);
	    updateLeftover(userData);
    	updateVitality(userData,0,0);
    	 if (userData == null || userData.Abilities == null) {
 		    userData.Abilities = new LinkedHashMap<>();}
    	int Levelish = (int)userData.getStat("Level")+1;
    	StringBuilder Abilities = new StringBuilder();
    	String raceName = userData.getCdata("Race");
    	if (raceName == null) {
    		raceName = "none";
    	}
    	for (int i = 0; i < userData.Abilities.size(); i++) {
    		 Map.Entry<String, String> entry = getEntryByIndex(userData.Abilities, i);
    		Abilities.append(entry.getKey()+ " "+"\n");
    		
    	}
    	if(userData.getStat("Level")==0) {
    		return  "Race: " + raceName + "\n" +
    				"Alignment: " + userData.Cdata.get("Alignment") +  "\n" +
    				"Unleveled" + "\n" +
    				"Total EXP: " + userData.getStat("EXP") + "\n" +
    				"EXP: " + userData.getStat("LeftoverEXP")+"/"+Levelish+ "\n"+ 
    				"\n"+ 
    				"Health(HP): " + userData.getStat("HP")+"/"+userData.getStat("Health") + "\n" +
    				"Vitality: " + userData.getStat("vitality") + "\n" +
    				"Strength: " + userData.getStat("strength") + "\n" +
                    "Dexterity: " + userData.getStat("dexterity") + "\n" +
                    "Agility: " + userData.getStat("agility") + "\n" +
                    "Endurance: " + userData.getStat("endurance") + "\n" +
                    "Willpower: " + userData.getStat("willpower") + "\n" +
                    "Magic: " + userData.getStat("magic") + "\n" +
                    "\n" +
                    "Remaining Points: " + userData.getStat("Remaining Points")  + "\n" + "\n" +
                    "__Current Abilities__" +"\n" + Abilities
                    		;
    	}
    	return 
    			"Race: " + raceName + "\n" +
    			"Alignment: " + userData.Cdata.get("Alignment") +  "\n" +
				"Level: " + userData.getStat("Level") + "\n" +
				"Total EXP: " + userData.getStat("EXP") + "\n" +
				"EXP: " + userData.getStat("LeftoverEXP")+"/"+Levelish+ "\n"+ 
				"\n"+ 
				"Health(HP): " + userData.getStat("HP")+"/"+userData.getStat("Health") + "\n" +
				"Vitality: " + userData.getStat("vitality") + "\n" +
				"Strength: " + userData.getStat("strength") + "\n" +
                "Dexterity: " + userData.getStat("dexterity") + "\n" +
                "Agility: " + userData.getStat("agility") + "\n" +
                "Endurance: " + userData.getStat("endurance") + "\n" +
                "Willpower: " + userData.getStat("willpower") + "\n" +
                "Magic: " + userData.getStat("magic") + "\n" +
                "\n" +
                "Remaining Points: " + userData.getStat("Remaining Points")  + "\n" + "\n" +
                "__Current Abilities__" +"\n" + Abilities
                		;
        		 
    }
    
    public static double calculateLeftover(int exp) {
    	int LEVEL = (int)Math.floor((-1 + Math.sqrt(1 + 8 * exp)) / 2);
    	int EXP = (int)exp;
        double leftover = EXP % (.5*LEVEL*LEVEL+.5*LEVEL);
		return leftover;
    }
    
    private void updateLeftover(UserData userData) {
        int exp = userData.getStat("EXP");
        int leftover = (int) calculateLeftover(exp);
        userData.setStat("LeftoverEXP", leftover, userData);
        saveUserData();
    }

    private void loadUserData() {
        try (FileReader reader = new FileReader(DATA_FILE)) {
            Type userDataType = new TypeToken<HashMap<String, UserData>>() {}.getType();
            userDataMap = new Gson().fromJson(reader, userDataType);
            
            if (userDataMap == null) {
                userDataMap = new HashMap<>();
            }
           
        } catch (IOException e) {
            userDataMap = new HashMap<>();
           
            e.printStackTrace();
        }
    }

    private void saveUserData() {
        try (FileWriter writer = new FileWriter(DATA_FILE)) {
            new Gson().toJson(userDataMap, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        private static final Random random = new Random();

        /**
         * Rolls a number between 1 and the given max value.
         *
         * @param max The maximum value for the roll (inclusive).
         * @return A random number between 1 and max (inclusive).
         */
        public static int roll(int max, MessageReceivedEvent event) {
        	  if (max < 1) {
              	event.getChannel().sendMessage("A value cant be 0").queue();
                  throw new IllegalArgumentException("Max value must be greater than 0");
              }
            return random.nextInt(max) + 1;
        }
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        private void rollprocessor(List<String> modifiers, List<String> dice, MessageReceivedEvent event, Member member) {
        	int result = 0;
        	double total2 =0;
        	int dividends = 0;
			int multipliers = 0;
			String Command2 = "";
			String totalCommand = "";
			String modifier = "";
			String dividendtext = "";
			String multiplierText = "";
			String DiceText = "";
			String Length = "";
			int number = 0;
			String userId = member.getId();
		    UserData userData = userDataMap.get(userId);
		    if (userData.Proxy) {
		    	userId = userData.ProxyUserID;
		    }
		    userData = userDataMap.get(userId);
        	List<Integer> total = new ArrayList<>();
        	List<Integer> Bonuses = new ArrayList<>();
        	String response = "";
        	
        	for(int i = 0; i < dice.size(); i++) {
        		String di = "";
        		
        		if (dice.get(i).contains("d")) {
        		di = dice.get(i);} 
        		else if (modifiers.get(i).equals("-")) { 
        				Bonuses.add(-(Integer.valueOf(dice.get(i))));
        		} else Bonuses.add(Integer.valueOf(dice.get(i)));
        		String diCheck = di.toLowerCase();
        	
        		if (diCheck.contains("strength")){
        		
        			int times = Character.getNumericValue(di.charAt(0));
        			if (di.charAt(2) == '9'||
    						di.charAt(2) == '1'
    						||di.charAt(2) ==  '2'
    						||di.charAt(2) ==  '3'
    						||di.charAt(2) ==  '4'
    						||di.charAt(2) ==  '5'
    						||di.charAt(2) ==  '6'
    						||di.charAt(2) ==  '7'
    						||di.charAt(2) ==  '8'
    						||di.charAt(2) ==  '0'
    						) {
        				
        				DiceText= String.valueOf(di.charAt(0))+String.valueOf(di.charAt(1)+String.valueOf(di.charAt(2)));
        			}else if (di.charAt(1) == '9'||
    						di.charAt(1) == '1'
    						||di.charAt(1) ==  '2'
    						||di.charAt(1) ==  '3'
    						||di.charAt(1) ==  '4'
    						||di.charAt(1) ==  '5'
    						||di.charAt(1) ==  '6'
    						||di.charAt(1) ==  '7'
    						||di.charAt(1) ==  '8'
    						||di.charAt(1) ==  '0') {
        				DiceText = String.valueOf(di.charAt(0))+String.valueOf(di.charAt(1));
        			} else
        			DiceText= String.valueOf(di.charAt(0));
        			times = Integer.valueOf(DiceText);
        		for (int i2 = 0; i2 < times; i2++) {
        			
        			if (di.contains("/")) {
        				dividends = Character.getNumericValue(di.charAt(di.length()-1));
        				
        				if (di.charAt(di.length()-2) == '9'||
        						di.charAt(di.length()-2) == '1'
        						||di.charAt(di.length()-2) ==  '2'
        						||di.charAt(di.length()-2) ==  '3'
        						||di.charAt(di.length()-2) ==  '4'
        						||di.charAt(di.length()-2) ==  '5'
        						||di.charAt(di.length()-2) ==  '6'
        						||di.charAt(di.length()-2) ==  '7'
        						||di.charAt(di.length()-2) ==  '8')
        				 {
        					dividendtext = String.valueOf(di.charAt(di.length()-2))+String.valueOf(di.charAt(di.length()-1));
        					
        					dividends = Integer.valueOf(dividendtext);
        					
        					}}
        				if (di.contains("*")) {
        				multipliers = Character. getNumericValue(di.charAt(di.length()-1));
        				
        				
        				if (di.charAt(di.length()-2) == '9'||
        						di.charAt(di.length()-2) == '1'
        						||di.charAt(di.length()-2) ==  '2'
        						||di.charAt(di.length()-2) ==  '3'
        						||di.charAt(di.length()-2) ==  '4'
        						||di.charAt(di.length()-2) ==  '5'
        						||di.charAt(di.length()-2) ==  '6'
        						||di.charAt(di.length()-2) ==  '7'
        						||di.charAt(di.length()-2) ==  '8'){
        					multiplierText = String.valueOf(di.charAt(di.length()-2))+String.valueOf(di.charAt(di.length()-1));
        					multipliers = Integer.valueOf(multiplierText);	
        				}}
        			if (dividends >0) {
        				
            			result = roll((int) userData.getStat("strength")/dividends, event);} 
        			else if(multipliers > 0) {
        				result = roll(userData.getStat("strength")*multipliers, event);
        			}else {result = roll(userData.getStat("strength"), event);}
        			total.add(result);
        			dividends = 0;
        			multipliers = 0;}}
        		if (diCheck.contains("vitality")){
        			int times = Character.getNumericValue(di.charAt(0));
        			if (di.charAt(2) == '9'||
    						di.charAt(2) == '1'
    						||di.charAt(2) ==  '2'
    						||di.charAt(2) ==  '3'
    						||di.charAt(2) ==  '4'
    						||di.charAt(2) ==  '5'
    						||di.charAt(2) ==  '6'
    						||di.charAt(2) ==  '7'
    						||di.charAt(2) ==  '8'
    						||di.charAt(2) ==  '0'
    						) {
        				
        				DiceText= String.valueOf(di.charAt(0))+String.valueOf(di.charAt(1)+String.valueOf(di.charAt(2)));
        			}else if (di.charAt(1) == '9'||
    						di.charAt(1) == '1'
    						||di.charAt(1) ==  '2'
    						||di.charAt(1) ==  '3'
    						||di.charAt(1) ==  '4'
    						||di.charAt(1) ==  '5'
    						||di.charAt(1) ==  '6'
    						||di.charAt(1) ==  '7'
    						||di.charAt(1) ==  '8'
    						||di.charAt(1) ==  '0') {
        				DiceText = String.valueOf(di.charAt(0))+String.valueOf(di.charAt(1));
        			} else
        			DiceText= String.valueOf(di.charAt(0));
        			times = Integer.valueOf(DiceText);
            		for (int i2 = 0; i2 < times; i2++) {
            			
            			if (di.contains("/")) {
            				dividends = Character.getNumericValue(di.charAt(di.length()-1));
            				
            				if (di.charAt(di.length()-2) == '9'||
            						di.charAt(di.length()-2) == '1'
            						||di.charAt(di.length()-2) ==  '2'
            						||di.charAt(di.length()-2) ==  '3'
            						||di.charAt(di.length()-2) ==  '4'
            						||di.charAt(di.length()-2) ==  '5'
            						||di.charAt(di.length()-2) ==  '6'
            						||di.charAt(di.length()-2) ==  '7'
            						||di.charAt(di.length()-2) ==  '8')
            				 {
            					dividendtext = String.valueOf(di.charAt(di.length()-2))+String.valueOf(di.charAt(di.length()-1));
            					
            					dividends = Integer.valueOf(dividendtext);
            					}}
            				if (di.contains("*")) {
            				multipliers = Character. getNumericValue(di.charAt(di.length()-1));
            				
            				
            				if (di.charAt(di.length()-2) == '9'||
            						di.charAt(di.length()-2) == '1'
            						||di.charAt(di.length()-2) ==  '2'
            						||di.charAt(di.length()-2) ==  '3'
            						||di.charAt(di.length()-2) ==  '4'
            						||di.charAt(di.length()-2) ==  '5'
            						||di.charAt(di.length()-2) ==  '6'
            						||di.charAt(di.length()-2) ==  '7'
            						||di.charAt(di.length()-2) ==  '8'){
            					multiplierText = String.valueOf(di.charAt(di.length()-2))+String.valueOf(di.charAt(di.length()-1));
            					multipliers = Integer.valueOf(multiplierText);				
            				}}
            			if (dividends >0) {
                			result = roll(userData.getStat("vitality")/dividends, event);} 
            			else if(multipliers > 0) {
            				result = roll(userData.getStat("vitality")*multipliers, event);
            			}else {result = roll(userData.getStat("vitality"), event);}
            			total.add(result);
            			dividends = 0;
            			multipliers = 0;}}
        		if (diCheck.contains("dexterity")){
        			int times = Character.getNumericValue(di.charAt(0));
        			if (di.charAt(2) == '9'||
    						di.charAt(2) == '1'
    						||di.charAt(2) ==  '2'
    						||di.charAt(2) ==  '3'
    						||di.charAt(2) ==  '4'
    						||di.charAt(2) ==  '5'
    						||di.charAt(2) ==  '6'
    						||di.charAt(2) ==  '7'
    						||di.charAt(2) ==  '8'
    						||di.charAt(2) ==  '0'
    						) {
        				
        				DiceText= String.valueOf(di.charAt(0))+String.valueOf(di.charAt(1)+String.valueOf(di.charAt(2)));
        			}else if (di.charAt(1) == '9'||
    						di.charAt(1) == '1'
    						||di.charAt(1) ==  '2'
    						||di.charAt(1) ==  '3'
    						||di.charAt(1) ==  '4'
    						||di.charAt(1) ==  '5'
    						||di.charAt(1) ==  '6'
    						||di.charAt(1) ==  '7'
    						||di.charAt(1) ==  '8'
    						||di.charAt(1) ==  '0') {
        				DiceText = String.valueOf(di.charAt(0))+String.valueOf(di.charAt(1));
        			} else
        			DiceText= String.valueOf(di.charAt(0));
        			times = Integer.valueOf(DiceText);
            		for (int i2 = 0; i2 < times; i2++) {
            			
            			if (di.contains("/")) {
            				dividends = Character.getNumericValue(di.charAt(di.length()-1));
            				
            				if (di.charAt(di.length()-2) == '9'||
            						di.charAt(di.length()-2) == '1'
            						||di.charAt(di.length()-2) ==  '2'
            						||di.charAt(di.length()-2) ==  '3'
            						||di.charAt(di.length()-2) ==  '4'
            						||di.charAt(di.length()-2) ==  '5'
            						||di.charAt(di.length()-2) ==  '6'
            						||di.charAt(di.length()-2) ==  '7'
            						||di.charAt(di.length()-2) ==  '8')
            				 {
            					dividendtext = String.valueOf(di.charAt(di.length()-2))+String.valueOf(di.charAt(di.length()-1));
            					
            					dividends = Integer.valueOf(dividendtext);
            					}}
            				if (di.contains("*")) {
            				multipliers = Character. getNumericValue(di.charAt(di.length()-1));
            				
            				
            				if (di.charAt(di.length()-2) == '9'||
            						di.charAt(di.length()-2) == '1'
            						||di.charAt(di.length()-2) ==  '2'
            						||di.charAt(di.length()-2) ==  '3'
            						||di.charAt(di.length()-2) ==  '4'
            						||di.charAt(di.length()-2) ==  '5'
            						||di.charAt(di.length()-2) ==  '6'
            						||di.charAt(di.length()-2) ==  '7'
            						||di.charAt(di.length()-2) ==  '8'){
            					multiplierText = String.valueOf(di.charAt(di.length()-2))+String.valueOf(di.charAt(di.length()-1));
            					multipliers = Integer.valueOf(multiplierText);		
            				}}
            			if (dividends >0) {
                			result = roll(userData.getStat("dexterity")/dividends, event);} 
            			else if(multipliers > 0) {
            				result = roll(userData.getStat("dexterity")*multipliers, event);
            			}else {result = roll(userData.getStat("dexterity"), event);}
            			total.add(result);
            			dividends = 0;
            			multipliers = 0;}}
        		if (diCheck.contains("agility")){
        			int times = Character.getNumericValue(di.charAt(0));
        			if (di.charAt(2) == '9'||
    						di.charAt(2) == '1'
    						||di.charAt(2) ==  '2'
    						||di.charAt(2) ==  '3'
    						||di.charAt(2) ==  '4'
    						||di.charAt(2) ==  '5'
    						||di.charAt(2) ==  '6'
    						||di.charAt(2) ==  '7'
    						||di.charAt(2) ==  '8'
    						||di.charAt(2) ==  '0'
    						) {
        				
        				DiceText= String.valueOf(di.charAt(0))+String.valueOf(di.charAt(1)+String.valueOf(di.charAt(2)));
        			}else if (di.charAt(1) == '9'||
    						di.charAt(1) == '1'
    						||di.charAt(1) ==  '2'
    						||di.charAt(1) ==  '3'
    						||di.charAt(1) ==  '4'
    						||di.charAt(1) ==  '5'
    						||di.charAt(1) ==  '6'
    						||di.charAt(1) ==  '7'
    						||di.charAt(1) ==  '8'
    						||di.charAt(1) ==  '0') {
        				DiceText = String.valueOf(di.charAt(0))+String.valueOf(di.charAt(1));
        			} else
        			DiceText= String.valueOf(di.charAt(0));
        			times = Integer.valueOf(DiceText);
            		for (int i2 = 0; i2<times; i2++) {
            			result = roll(userData.getStat("agility"), event);
            			if (di.contains("/")) {
            				int dividend = Character.getNumericValue(di.charAt(di.length()-1));
            				int resultStorage2 = result;
            				result = resultStorage2/dividend;
            				if (di.charAt(di.length()-2) == '9'||
            						di.charAt(di.length()-2) == '1'
            						||di.charAt(di.length()-2) ==  '2'
            						||di.charAt(di.length()-2) ==  '3'
            						||di.charAt(di.length()-2) ==  '4'
            						||di.charAt(di.length()-2) ==  '5'
            						||di.charAt(di.length()-2) ==  '6'
            						||di.charAt(di.length()-2) ==  '7'
            						||di.charAt(di.length()-2) ==  '8') {
            					dividendtext = String.valueOf(di.charAt(di.length()-2))+String.valueOf(di.charAt(di.length()-1));
            					
            					dividends = Integer.valueOf(dividendtext);
            					
            					if(result == 0) {
            						result =1;
            					}
            				}}
            			if (di.contains("*")) {
            				int multiplier = Character. getNumericValue(di.charAt(di.length()-1));
            				int resultStorage2 = result;
            				result = resultStorage2*multiplier;
            				if (di.charAt(di.length()-2) == '9'||
            						di.charAt(di.length()-2) == '1'
            						||di.charAt(di.length()-2) ==  '2'
            						||di.charAt(di.length()-2) ==  '3'
            						||di.charAt(di.length()-2) ==  '4'
            						||di.charAt(di.length()-2) ==  '5'
            						||di.charAt(di.length()-2) ==  '6'
            						||di.charAt(di.length()-2) ==  '7'
            						||di.charAt(di.length()-2) ==  '8') {
            					multiplierText = String.valueOf(di.charAt(di.length()-2))+String.valueOf(di.charAt(di.length()-1));
            					multipliers = Integer.valueOf(multiplierText);	
            					}
            				}if (dividends >0) {
            					double Holder = userData.getStat("agility")*1.5/dividends;
                    			result = roll((int)Math.round(Holder), event);} 
                			else if(multipliers > 0) {
                				double Holder = userData.getStat("agility")*1.5*multipliers;
                				result = roll((int)Math.round(Holder), event);
                			}else {double Holder = userData.getStat("agility")*1.5;
                				result = roll((int)Math.round(Holder), event);}
                			total.add(result);
                			dividends = 0;
                			multipliers = 0;}}
        		if (diCheck.contains("endurance")){
        			int times = Character.getNumericValue(di.charAt(0));
        			if (di.charAt(2) == '9'||
    						di.charAt(2) == '1'
    						||di.charAt(2) ==  '2'
    						||di.charAt(2) ==  '3'
    						||di.charAt(2) ==  '4'
    						||di.charAt(2) ==  '5'
    						||di.charAt(2) ==  '6'
    						||di.charAt(2) ==  '7'
    						||di.charAt(2) ==  '8'
    						||di.charAt(2) ==  '0'
    						) {
        				
        				DiceText= String.valueOf(di.charAt(0))+String.valueOf(di.charAt(1)+String.valueOf(di.charAt(2)));
        			}else if (di.charAt(1) == '9'||
    						di.charAt(1) == '1'
    						||di.charAt(1) ==  '2'
    						||di.charAt(1) ==  '3'
    						||di.charAt(1) ==  '4'
    						||di.charAt(1) ==  '5'
    						||di.charAt(1) ==  '6'
    						||di.charAt(1) ==  '7'
    						||di.charAt(1) ==  '8'
    						||di.charAt(1) ==  '0') {
        				DiceText = String.valueOf(di.charAt(0))+String.valueOf(di.charAt(1));
        			} else
        			DiceText= String.valueOf(di.charAt(0));
        			times = Integer.valueOf(DiceText);
            		for (int i2 = 0; i2 < times; i2++) {
            			
            			if (di.contains("/")) {
            				dividends = Character.getNumericValue(di.charAt(di.length()-1));
            				
            				if (di.charAt(di.length()-2) == '9'||
            						di.charAt(di.length()-2) == '1'
            						||di.charAt(di.length()-2) ==  '2'
            						||di.charAt(di.length()-2) ==  '3'
            						||di.charAt(di.length()-2) ==  '4'
            						||di.charAt(di.length()-2) ==  '5'
            						||di.charAt(di.length()-2) ==  '6'
            						||di.charAt(di.length()-2) ==  '7'
            						||di.charAt(di.length()-2) ==  '8')
            				 {
            					dividendtext = String.valueOf(di.charAt(di.length()-2))+String.valueOf(di.charAt(di.length()-1));
            					
            					dividends = Integer.valueOf(dividendtext);
            					}}
            				if (di.contains("*")) {
            				multipliers = Character. getNumericValue(di.charAt(di.length()-1));
            				
            				
            				if (di.charAt(di.length()-2) == '9'||
            						di.charAt(di.length()-2) == '1'
            						||di.charAt(di.length()-2) ==  '2'
            						||di.charAt(di.length()-2) ==  '3'
            						||di.charAt(di.length()-2) ==  '4'
            						||di.charAt(di.length()-2) ==  '5'
            						||di.charAt(di.length()-2) ==  '6'
            						||di.charAt(di.length()-2) ==  '7'
            						||di.charAt(di.length()-2) ==  '8'){
            					multiplierText = String.valueOf(di.charAt(di.length()-2))+String.valueOf(di.charAt(di.length()-1));
            					multipliers = Integer.valueOf(multiplierText);				
            				}}
            			if (dividends >0) {
                			result = roll(userData.getStat("endurance")/dividends, event);} 
            			else if(multipliers > 0) {
            				result = roll(userData.getStat("endurance")*multipliers, event);
            			}else {result = roll(userData.getStat("endurance"), event);}
            			total.add(result);
            			dividends = 0;
            			multipliers = 0;}}
        		if (diCheck.contains("magic")){
        			int times = Character.getNumericValue(di.charAt(0));
        			if (di.charAt(2) == '9'||
    						di.charAt(2) == '1'
    						||di.charAt(2) ==  '2'
    						||di.charAt(2) ==  '3'
    						||di.charAt(2) ==  '4'
    						||di.charAt(2) ==  '5'
    						||di.charAt(2) ==  '6'
    						||di.charAt(2) ==  '7'
    						||di.charAt(2) ==  '8'
    						||di.charAt(2) ==  '0'
    						) {
        				
        				DiceText= String.valueOf(di.charAt(0))+String.valueOf(di.charAt(1)+String.valueOf(di.charAt(2)));
        			}else if (di.charAt(1) == '9'||
    						di.charAt(1) == '1'
    						||di.charAt(1) ==  '2'
    						||di.charAt(1) ==  '3'
    						||di.charAt(1) ==  '4'
    						||di.charAt(1) ==  '5'
    						||di.charAt(1) ==  '6'
    						||di.charAt(1) ==  '7'
    						||di.charAt(1) ==  '8'
    						||di.charAt(1) ==  '0') {
        				DiceText = String.valueOf(di.charAt(0))+String.valueOf(di.charAt(1));
        			} else
        			DiceText= String.valueOf(di.charAt(0));
        			times = Integer.valueOf(DiceText);
            		
            		for (int i2 = 0; i2 < times; i2++) {
            			
            			if (di.contains("/")) {
            				dividends = Character.getNumericValue(di.charAt(di.length()-1));
            				
            				if (di.charAt(di.length()-2) == '9'||
            						di.charAt(di.length()-2) == '1'
            						||di.charAt(di.length()-2) ==  '2'
            						||di.charAt(di.length()-2) ==  '3'
            						||di.charAt(di.length()-2) ==  '4'
            						||di.charAt(di.length()-2) ==  '5'
            						||di.charAt(di.length()-2) ==  '6'
            						||di.charAt(di.length()-2) ==  '7'
            						||di.charAt(di.length()-2) ==  '8')
            				 {
            					dividendtext = String.valueOf(di.charAt(di.length()-2))+String.valueOf(di.charAt(di.length()-1));
            					
            					dividends = Integer.valueOf(dividendtext);
            					}}
            				if (di.contains("*")) {
            				multipliers = Character. getNumericValue(di.charAt(di.length()-1));
            				
            				
            				if (di.charAt(di.length()-2) == '9'||
            						di.charAt(di.length()-2) == '1'
            						||di.charAt(di.length()-2) ==  '2'
            						||di.charAt(di.length()-2) ==  '3'
            						||di.charAt(di.length()-2) ==  '4'
            						||di.charAt(di.length()-2) ==  '5'
            						||di.charAt(di.length()-2) ==  '6'
            						||di.charAt(di.length()-2) ==  '7'
            						||di.charAt(di.length()-2) ==  '8'){
            					multiplierText = String.valueOf(di.charAt(di.length()-2))+String.valueOf(di.charAt(di.length()-1));
            					multipliers = Integer.valueOf(multiplierText);	
            				}}
            			if (dividends >0) {
                			result = roll(userData.getStat("magic")/dividends, event);} 
            			else if(multipliers > 0) {
            				result = roll(userData.getStat("magic")*multipliers, event);
            			}else {result = roll(userData.getStat("magic"), event);}
            			
            			if (modifiers.get(i).contains("-")) {
            				int resultnegative = result*-1;
                			total.add(resultnegative);}
            			else total.add(result);
            			dividends = 0;
            			multipliers = 0;}}
        		
        		if (diCheck.contains("willpower")){
        			int times = Character.getNumericValue(di.charAt(0));
        			if (di.charAt(2) == '9'||
    						di.charAt(2) == '1'
    						||di.charAt(2) ==  '2'
    						||di.charAt(2) ==  '3'
    						||di.charAt(2) ==  '4'
    						||di.charAt(2) ==  '5'
    						||di.charAt(2) ==  '6'
    						||di.charAt(2) ==  '7'
    						||di.charAt(2) ==  '8'
    						||di.charAt(2) ==  '0'
    						) {
        				
        				DiceText= String.valueOf(di.charAt(0))+String.valueOf(di.charAt(1)+String.valueOf(di.charAt(2)));
        			}else if (di.charAt(1) == '9'||
    						di.charAt(1) == '1'
    						||di.charAt(1) ==  '2'
    						||di.charAt(1) ==  '3'
    						||di.charAt(1) ==  '4'
    						||di.charAt(1) ==  '5'
    						||di.charAt(1) ==  '6'
    						||di.charAt(1) ==  '7'
    						||di.charAt(1) ==  '8'
    						||di.charAt(1) ==  '0') {
        				DiceText = String.valueOf(di.charAt(0))+String.valueOf(di.charAt(1));
        			} else
        			DiceText= String.valueOf(di.charAt(0));
        			times = Integer.valueOf(DiceText);
            		for (int i2 = 0; i2 < times; i2++) {
            			
            			if (di.contains("/")) {
            				dividends = Character.getNumericValue(di.charAt(di.length()-1));
            				
            				if (di.charAt(di.length()-2) == '9'||
            						di.charAt(di.length()-2) == '1'
            						||di.charAt(di.length()-2) ==  '2'
            						||di.charAt(di.length()-2) ==  '3'
            						||di.charAt(di.length()-2) ==  '4'
            						||di.charAt(di.length()-2) ==  '5'
            						||di.charAt(di.length()-2) ==  '6'
            						||di.charAt(di.length()-2) ==  '7'
            						||di.charAt(di.length()-2) ==  '8')
            				 {
            					dividendtext = String.valueOf(di.charAt(di.length()-2))+String.valueOf(di.charAt(di.length()-1));
            					
            					dividends = Integer.valueOf(dividendtext);
            					}}
            				if (di.contains("*")) {
            				multipliers = Character. getNumericValue(di.charAt(di.length()-1));
            				
            				
            				if (di.charAt(di.length()-2) == '9'||
            						di.charAt(di.length()-2) == '1'
            						||di.charAt(di.length()-2) ==  '2'
            						||di.charAt(di.length()-2) ==  '3'
            						||di.charAt(di.length()-2) ==  '4'
            						||di.charAt(di.length()-2) ==  '5'
            						||di.charAt(di.length()-2) ==  '6'
            						||di.charAt(di.length()-2) ==  '7'
            						||di.charAt(di.length()-2) ==  '8'){
            					multiplierText = String.valueOf(di.charAt(di.length()-2))+String.valueOf(di.charAt(di.length()-1));
            					multipliers = Integer.valueOf(multiplierText);		
            				}}
            			if (dividends >0) {
                			result = roll(userData.getStat("willpower")/dividends, event);} 
            			else if(multipliers > 0) {
            				result = roll(userData.getStat("willpower")*multipliers, event);
            			}else {result = roll(userData.getStat("willpower"), event);}
            			total.add(result);dividends = 0;
            			multipliers = 0;}}
        		}
        	for (int i = 0; i<total.size();i++) {
        		number = total.get(i);
        		
            		total2 += number;
        		if (i != total.size()-1) {
        			response = response + "(" +String.valueOf(number) +")" + " + ";
        		}else response = response + "(" +String.valueOf(number) +")";}
        	for (int i = 0; i<Bonuses.size();i++) {
        		number = Bonuses.get(i);
        		
            		total2 += number;
            		if (i == 0) {
            			response = response + " + "+ "(" +String.valueOf(number) +")";
            		}
            		else if (i != Bonuses.size()-1) {
        			response = response + " + " +"(" +String.valueOf(number) +")";
        		}else response = response + " + "+ "(" +String.valueOf(number) +")";}
        		String totaltext = String.valueOf(total2);
        		for (int i5=0; i5 < dice.size(); i5++) {
        			if (i5 > 0) {
        			}
        			if (i5 <modifiers.size()-1) {
        			
        			modifier = modifiers.get(i5+1);
        			
        			}
        			
        			Command2 = dice.get(i5);
        			if (modifier == null) {
        				totalCommand = Command2;
        			}else  if ((Command2 == dice.get(0))) {
        				totalCommand = Command2 + " " + modifier;
        				
        			} else if (Command2 != dice.get(dice.size()-1)) {
        				totalCommand = totalCommand + " "+Command2 + " " + modifier;
        			} else {
        				totalCommand = totalCommand + " "+ Command2;
        			}
        		}
        		Length = totalCommand + " = " + response + " = " + totaltext;
        	if (Length.length() < 4096) {
        	event.getChannel().sendMessageEmbeds(createEmbed(member.getEffectiveName() + "'s Roll = " + totaltext,
        			totalCommand + " = " + response + " = " + totaltext)).queue();}
        	else {
        		event.getChannel().sendMessage("Please keep the roll to below 4096 characters").queue();
        	}
        	
}public static List<String> splitByQuotes(String message) {
    List<String> segments = new ArrayList<>();

    Pattern pattern = Pattern.compile("\"([^\"]*)\"");
    Matcher matcher = pattern.matcher(message);

    int lastIndex = 0;
    while (matcher.find()) {
        // Add text before the quoted segment
        if (matcher.start() > lastIndex) {
            String textBeforeQuote = message.substring(lastIndex, matcher.start()).trim();
            if (!textBeforeQuote.isEmpty()) {
                segments.add(textBeforeQuote);
            }
        }
        // Add the quoted segment itself (without quotes)
        segments.add(matcher.group(1));
        lastIndex = matcher.end();
    }

    // Add any text after the last quoted segment
    if (lastIndex < message.length()) {
        String textAfterQuote = message.substring(lastIndex).trim();
        if (!textAfterQuote.isEmpty()) {
            segments.add(textAfterQuote);
        }
    }

    return segments;
}

private void addAbility(Member targetMember, String abilityName, String Abilitytext, MessageReceivedEvent event) {
	if (targetMember == null) {
		targetMember = event.getMember();}
	String userId = targetMember.getId();
    UserData userData = userDataMap.get(userId);
    if (userData.Proxy) {
    	userId = userData.ProxyUserID;
    }
    userData = userDataMap.get(userId);
    if (userData == null || userData.Abilities == null) {
	    // Handle the situation where userData or Cdata is null
	    // For example, initialize Cdata explicitly if it can be null
	    userData.Abilities = new LinkedHashMap<>();
    }

    userData.Abilities.put(abilityName, Abilitytext);
    saveUserData();
    event.getChannel().sendMessageEmbeds(createEmbed("Ability Added",abilityName + " has been added.")).queue();
}
public static <K, V> Map.Entry<K, V> getEntryByIndex(LinkedHashMap<K, V> map, int index) {
    int currentIndex = 0;
    for (Map.Entry<K, V> entry : map.entrySet()) {
        if (currentIndex == index) {
            return entry;
        }
        currentIndex++;
    }
    return null; // Return null if the index is out of bounds
}
private void removeAbility(String Ability, Member targetMember, MessageReceivedEvent event) {
	if (targetMember == null) {
		targetMember = event.getMember();}
	String userId = targetMember.getId();
    UserData userData = userDataMap.get(userId);
    if (userData.Proxy) {
    	userId = userData.ProxyUserID;
    }

    userData = userDataMap.get(userId);
	    if (userData == null || userData.Abilities == null) {
		    // Handle the situation where userData or Cdata is null
		    // For example, initialize Cdata explicitly if it can be null
		    userData.Abilities = new LinkedHashMap<>();
	    } 
		    String keyToRemove = Ability;
	        userData.Abilities.remove(keyToRemove);
	        event.getChannel().sendMessage(keyToRemove + " has been removed!").queue();
	        saveUserData();
	    
}
private void viewAbility(String abilityName, Member targetMember, MessageReceivedEvent event) {
	if (targetMember == null) {
		targetMember = event.getMember();}
	String userId = targetMember.getId();
    UserData userData = userDataMap.get(userId);
    if (userData.Proxy) {
    	userId = userData.ProxyUserID;
    }

    userData = userDataMap.get(userId);
	    if (userData == null || userData.Abilities == null) {
		    // Handle the situation where userData or Cdata is null
		    // For example, initialize Cdata explicitly if it can be null
		    userData.Abilities = new LinkedHashMap<>();}
	    event.getChannel().sendMessageEmbeds(createEmbed("Ability: "+ abilityName, userData.Abilities.get(abilityName))).queue();
	    saveUserData();
	    
}
private void renameAbility(String currentName, String newName, Member member, MessageReceivedEvent event) {
	String userId = member.getId();
    UserData userData = userDataMap.get(userId);
    if (userData.Proxy) {
    	userId = userData.ProxyUserID;
    }

    userData = userDataMap.get(userId);
	    if (userData == null || userData.Abilities == null) {
		    // Handle the situation where userData or Cdata is null
		    // For example, initialize Cdata explicitly if it can be null
		    userData.Abilities = new LinkedHashMap<>();}
	  replaceKey(userData.Abilities, currentName, newName);
	  event.getChannel().sendMessage(currentName + " has been replaced by " + newName).queue();   
	  saveUserData();
}
public static <K, V> void replaceKey(LinkedHashMap<K, V> map, K oldKey, K newKey) {
    if (!map.containsKey(oldKey)) {
        throw new IllegalArgumentException("The old key does not exist in the map.");
    }
    if (map.containsKey(newKey)) {
        throw new IllegalArgumentException("The new key already exists in the map.");
    }

    // Retrieve the value associated with the old key
    V value = map.get(oldKey);

    // Insert the new key-value pair
    LinkedHashMap<K, V> tempMap = new LinkedHashMap<>();

    for (Map.Entry<K, V> entry : map.entrySet()) {
        if (entry.getKey().equals(oldKey)) {
            tempMap.put(newKey, value);
        } else {
            tempMap.put(entry.getKey(), entry.getValue());
        }
    }

    // Replace the old map with the modified one
    map.clear();
    map.putAll(tempMap);
   
}
private void EquipArmor(Member member, MessageReceivedEvent event, String ArmorName, String Bonus) {
	
	String userId = member.getId();
    UserData userData = userDataMap.get(userId);
    if (userData.Proxy) {
    	userId = userData.ProxyUserID;
    }
if (userData.Armor==null) {
	userData.Armor = new LinkedHashMap<>();
}
if (ArmorName.equals("") || ArmorName==null) {
	userData.Armor.clear();
	return;
} else {
	
    userData = userDataMap.get(userId);
	
	userData.Armor.clear();
    userData.Armor.put(ArmorName, Bonus);
    
    event.getChannel().sendMessage("Armor " + ArmorName + " has been added!").queue();   
    saveUserData();
	
}}
private void EquipWeapon(Member member, MessageReceivedEvent event, String WeaponName, String Bonus) {
	String userId = member.getId();
    UserData userData = userDataMap.get(userId);
    if (userData.Proxy) {
    	userId = userData.ProxyUserID;
    }
    
    
    if (userData.Weapon==null) {
    	userData.Weapon = new LinkedHashMap<>();
    }
    if (WeaponName==null) {
    	userData.Weapon.clear();
    	return;} else {
    userData = userDataMap.get(userId);
	userData.Weapon.clear();
	userData.Weapon.put(WeaponName, Bonus);
	event.getChannel().sendMessage("Weapon " + WeaponName + " has been added!").queue();   
	
}}
private void EquipEnchanted(Member member, MessageReceivedEvent event, String EnchantedName, String Bonus) {
	String userId = member.getId();
    UserData userData = userDataMap.get(userId);
    if (userData.Proxy) {
    	userId = userData.ProxyUserID;
    }

    userData = userDataMap.get(userId);
    if (userData.Enchant==null) {
    	userData.Enchant = new LinkedHashMap<>();
    } else
    
    if (EnchantedName==null) {
    	userData.Enchant.clear();
    	return;} else {
    userData.Enchant.clear();
    userData.Enchant.put(EnchantedName, Bonus);
    event.getChannel().sendMessage("Item " + EnchantedName + " has been added!").queue();   
      
}}
private void viewEquipment(Member Member, MessageReceivedEvent event) {
	String userId = Member.getId();
    UserData userData = userDataMap.get(userId);
    if (userData.Proxy) {
    	userId = userData.ProxyUserID;
    }

    userData = userDataMap.get(userId);
	    if (userData == null || userData.Armor == null) {
		    // Handle the situation where userData or Cdata is null
		    // For example, initialize Cdata explicitly if it can be null
		    userData.Armor = new LinkedHashMap<>();
	    }
	    if (userData == null || userData.Weapon == null) {
		    // Handle the situation where userData or Cdata is null
		    // For example, initialize Cdata explicitly if it can be null
		    userData.Weapon = new LinkedHashMap<>();
	    }
	    if (userData == null || userData.Enchant == null) {
		    // Handle the situation where userData or Cdata is null
		    // For example, initialize Cdata explicitly if it can be null
		    userData.Enchant = new LinkedHashMap<>();
	    }
	    StringBuilder Inventory = new StringBuilder();
		Entry<String, String> variable = getEntryByIndex(userData.Armor, 0);
		Entry<String, String> variable2 = getEntryByIndex(userData.Weapon, 0);
		Entry<String, String> variable3 = getEntryByIndex(userData.Enchant, 0);
		String Variable ="None";
		String Variable2="None";
		String Variable3="None";
		if (variable !=null) {
			if (!variable.getKey().equals(""))
		Variable = variable.getKey();}
		if (variable2 !=null) {
			if (!variable2.getKey().equals(""))
		Variable2 = variable2.getKey();}
		
		if (variable3 !=null) {
			if (!variable3.getKey().equals(""))
		Variable3 = variable3.getKey();}
		Inventory.append(Variable + " " + userData.Armor.get(Variable) + "\n" +
				Variable2 + " " + userData.Weapon.get(Variable2) + "\n"+
				Variable3 + " " + userData.Enchant.get(Variable3) + "\n");
	
	
	 event.getChannel().sendMessageEmbeds(createEmbed("Gear", Inventory.toString())).queue();
	 saveUserData();
}
private void addBag(String ItemName, String ItemDescription, Member Member, MessageReceivedEvent event) {
	String userId = Member.getId();
    UserData userData = userDataMap.get(userId);
    if (userData.Proxy) {
    	userId = userData.ProxyUserID;
    }

    userData = userDataMap.get(userId);
	    if (userData == null || userData.Bag == null) {
		    // Handle the situation where userData or Cdata is null
		    // For example, initialize Cdata explicitly if it can be null
		    userData.Bag = new LinkedHashMap<>();
}
	    userData.Bag.put(ItemName, ItemDescription);
	    saveUserData();
}
private void RemoveBag(String ItemName, Member Member, MessageReceivedEvent event) {
	String userId = Member.getId();
    UserData userData = userDataMap.get(userId);
    if (userData.Proxy) {
    	userId = userData.ProxyUserID;
    }

    userData = userDataMap.get(userId);
	    if (userData == null || userData.Bag == null) {
		    // Handle the situation where userData or Cdata is null
		    // For example, initialize Cdata explicitly if it can be null
		    userData.Bag = new LinkedHashMap<>();
}
	    userData.Bag.remove(ItemName);
	    saveUserData();
}
private void viewBag(Member Member, MessageReceivedEvent event, Map<String,String> map) {
	String userId = Member.getId();
    UserData userData = userDataMap.get(userId);
    if (userData.Proxy) {
    	userId = userData.ProxyUserID;
    }

    userData = userDataMap.get(userId);
		    if (userData == null || userData.Bag == null) {
			    // Handle the situation where userData or Cdata is null
			    // For example, initialize Cdata explicitly if it can be null
			    userData.Bag = new LinkedHashMap<>();
		    }
		    StringBuilder Inventory = new StringBuilder();
		for (int i = 0; i < userData.Bag.size(); i++) {
			Entry<String, String> variable = getEntryByIndex(userData.Bag, i);
			String Variable = variable.getKey();
			Inventory.append(variable + " " + userData.Bag.get(Variable) + "\n");
		}
		
		 event.getChannel().sendMessageEmbeds(createEmbed("Inventory", Inventory.toString())).queue();
	}
private void coinReturn(Member Member, MessageReceivedEvent event, String CoinType, int amount) {
	if (Member == null) {
		Member = event.getMember();
	}
	int value = 0;
    int value2 = 0;
    int value3 = 0;
    int value4 = 0;
    String userId = Member.getId();
    UserData userData = userDataMap.get(userId);
    if (userData.Proxy) {
    	userId = userData.ProxyUserID;
    }

    userData = userDataMap.get(userId);
	    if (userData == null || userData.Currency == null) {
		    // Handle the situation where userData or Cdata is null
		    // For example, initialize Cdata explicitly if it can be null
		    userData.Currency = new LinkedHashMap<>();
		  
	    }
	    CoinType = CoinType.toLowerCase();
	    userData.Currency.put(CoinType, amount);
	    if (userData.Currency.get("stellar-gold") == null) {
   			 value = 0;
   		 } else  value = userData.Currency.get("stellar-gold");
   		 if (userData.Currency.get("gold") == null) {
   			 value2 = 0;} else  value2 = userData.Currency.get("gold");
   		 if (userData.Currency.get("silver") == null) {
   			 value3 = 0;} else  value3 = userData.Currency.get("silver");
   		 if (userData.Currency.get("bronze") == null) {
   			 value4 = 0;} else value4 = userData.Currency.get("bronze");
	    event.getChannel().sendMessageEmbeds(createEmbed("Coin Inventory", 
	    		"Current Inventory of coins" + "\n" +"\n" +
	    		"Stellar-Gold - " + value + "\n" +"\n" +
	    "Gold - " + value2 + "\n" +"\n" +
	    "Silver - " + value3 + "\n" +"\n" +
	    "bronze - " + value4
	    		)).queue();
	    saveUserData();
}
private void coinView(Member Member, MessageReceivedEvent event) {
	if (Member == null) {
		Member = event.getMember();
	}
	int value = 0;
    int value2 = 0;
    int value3 = 0;
    int value4 = 0;
    String userId = Member.getId();
    UserData userData = userDataMap.get(userId);
    if (userData.Proxy) {
    	userId = userData.ProxyUserID;
    }
    userData = userDataMap.get(userId);
    if (userData == null || userData.Currency == null) {
	    // Handle the situation where userData or Cdata is null
	    // For example, initialize Cdata explicitly if it can be null
	    userData.Currency = new LinkedHashMap<>();}
	  
	  
   		 if (userData.Currency.get("stellar-hold") == null) {
   			 value = 0;
   		 } else  value = userData.Currency.get("stellar-gold");
   		 if (userData.Currency.get("gold") == null) {
   			 value2 = 0;} else  value2 = userData.Currency.get("gold");
   		 if (userData.Currency.get("silver") == null) {
   			 value3 = 0;} else  value3 = userData.Currency.get("silver");
   		 if (userData.Currency.get("bronze") == null) {
   			 value4 = 0;} else value4 = userData.Currency.get("bronze");
   		 
   		 
   		
   		
   		
   		

	
	 event.getChannel().sendMessageEmbeds(createEmbed("Coin Inventory", 
	    		"Current Inventory of coins" + "\n" +"\n" +
	    		"Stellar-Gold - " + value + "\n" +"\n" +
	    "Gold - " + value2 + "\n" +"\n" +
	    "Silver - " + value3 + "\n" +"\n" +
	    "bronze - " + value4
	    		)).queue();
	    
	 saveUserData();
}
private void coinGive(Member targetMember, Member self, MessageReceivedEvent event, String CoinType, int amount) {
	String userId = targetMember.getId();
    UserData userData = userDataMap.get(userId);
    if (userData.Proxy) {
    	userId = userData.ProxyUserID;
    }

	    if (userData == null || userData.Currency == null) {
		    // Handle the situation where userData or Cdata is null
		    // For example, initialize Cdata explicitly if it can be null
		    userData.Currency = new LinkedHashMap<>();
		    int value = 0;
		 
		    String targetMemberID = targetMember.getId();
		    userDataMap.putIfAbsent(userId, new UserData(targetMemberID));
		    UserData userData2 = userDataMap.get(targetMemberID);
		    if (userData2 == null || userData2.Currency == null) {
			 
			    userData2.Currency = new LinkedHashMap<>();}
		    CoinType = CoinType.toLowerCase();
		    if (amount < userData.getStat(CoinType) || amount < 0) {
		    	 userData2.Currency.put(CoinType, amount);
		    	int loss= userData.Currency.get(CoinType) - amount;
		    	 userData.Currency.put(CoinType, loss);
		    	 String currencyName = "";
		    	 if (CoinType == "stellar-gold") {
		    		 currencyName = "Stellar-Gold";
		    		 if (userData.Currency.get(CoinType) == null) {
		    			 value = 0;
		    		 } 
		    	 }
		    	 if (CoinType == "gold") {
		    		 currencyName = "Gold";
		    		 if (userData.Currency.get(CoinType) == null) {
		    			 value = 0;}
		    	 }
		    	 if (CoinType == "silver") {
		    		 currencyName = "Silver";
		    		 if (userData.Currency.get(CoinType) == null) {
		    			 value = 0;}
		    	 }
		    	 if (CoinType == "bronze") {
		    		 currencyName = "Bronze";
		    		 if (userData.Currency.get(CoinType) == null) {
		    			 value = 0;}
		    	 }
		    	 if (userData.Currency.get(CoinType)!=null) {
		    	 value = userData.Currency.get(CoinType);}
		    	 
		    	 event.getChannel().sendMessageEmbeds(createEmbed(self.getEffectiveName() + " has given " + amount + " to " + targetMember.getEffectiveName(),"Current value" + "\n"+ currencyName +" - "+ value + "\n" +"\n")).queue();
		    	 
		    } else event.getChannel().sendMessage("You do not have enough coins").queue();
		    saveUserData();
}
}
private void cointake(Member targetMember, MessageReceivedEvent event, String CoinType, int amount) {
	String userId = targetMember.getId();
    UserData userData = userDataMap.get(userId);
    if (userData.Proxy) {
    	userId = userData.ProxyUserID;
    }

	    if (userData == null || userData.Currency == null) {
		    // Handle the situation where userData or Cdata is null
		    // For example, initialize Cdata explicitly if it can be null
		    userData.Currency = new LinkedHashMap<>();
		    
			 
			    userData.Currency = new LinkedHashMap<>();}
		    CoinType = CoinType.toLowerCase();
		  
		    	int loss= userData.Currency.get(CoinType) - amount;
		    	 userData.Currency.put(CoinType, loss);
		    	 String currencyName = "";
		    	 if (CoinType == "stellar-gold") {
		    		 currencyName = "Stellar-Gold";
		    	 }
		    	 if (CoinType == "gold") {
		    		 currencyName = "Gold";
		    	 }
		    	 if (CoinType == "silver") {
		    		 currencyName = "Silver";
		    	 }
		    	 if (CoinType == "bronze") {
		    		 currencyName = "Bronze";
		    	 }
		    	 if (userData.Currency.get(CoinType) <0) {
		    		 userData.Currency.put(currencyName, 0);
		    	 }
		    	 event.getChannel().sendMessageEmbeds(createEmbed(targetMember.getEffectiveName() + " has lost " + amount,"Current value" + "\n"+ currencyName +" - "+ userData.Currency.get(CoinType) + "\n" +"\n")).queue();
		    	 
		    	 saveUserData();

}
private void setAlignment(Member targetMember, MessageReceivedEvent event, String newAlignment) {
	if (targetMember == null) {
		targetMember = event.getMember();
	}
	String userId = targetMember.getId();
    UserData userData = userDataMap.get(userId);
    if (userData.Proxy) {
    	userId = userData.ProxyUserID;
    }

    userData = userDataMap.get(userId);
	    if (userData == null || userData.Cdata == null) {
		    // Handle the situation where userData or Cdata is null
		    // For example, initialize Cdata explicitly if it can be null
		    userData.Cdata = new LinkedHashMap<>();}
	   userData.Cdata.put("Alignment", newAlignment);
event.getChannel().sendMessageEmbeds(createEmbed("Alignment",targetMember.getEffectiveName() + "'s Alignment is now " + newAlignment+"\n" +"\n")).queue();
saveUserData();
 }

private void checkEvolution(Member member, MessageReceivedEvent event, int oldLevel, int newlevel) {

	String userId = member.getId();
    UserData userData = userDataMap.get(userId);
    if (userData.Proxy) {
    	userId = userData.ProxyUserID;
    }

    userData = userDataMap.get(userId);
	    if (userData.Cdata == null) {
		    // Handle the situation where userData or Cdata is null
		    // For example, initialize Cdata explicitly if it can be null
		    userData.Cdata = new LinkedHashMap<>();}
	    if (userData.stats == null) {
		    // Handle the situation where userData or Cdata is null
		    // For example, initialize Cdata explicitly if it can be null
		    userData.stats = new HashMap<>();}
	    if (userData.Abilities == null) {
		    // Handle the situation where userData or Cdata is null
		    // For example, initialize Cdata explicitly if it can be null
		    userData.Abilities = new LinkedHashMap<>();}
	    if (userData.availableEvolutions == null) {
		    // Handle the situation where userData or Cdata is null
		    // For example, initialize Cdata explicitly if it can be null
		    userData.availableEvolutions = new ArrayList<>();}
	    
	    if (userData.Cdata.get("Race").equals("Human") && newlevel >25) {
	    	if (!userData.availableEvolutions.contains("High Human")) {
	    	userData.availableEvolutions.add("High Human");}
	    }
	    if (userData.Cdata.get("Race").equals( "High Human") && newlevel >70) {
	    	if (!userData.availableEvolutions.contains("Divine Human")) {
	    	userData.availableEvolutions.add("Divine Human");}
	    }
	    if ((userData.Cdata.get("Race").equals("Divine Human") ||userData.Cdata.get("Race").equals("High Human")) && newlevel >70 && (userData.Cdata.get("Alignment").contains("Chaotic") ||userData.Cdata.get("Alignment").contains("Evil"))) {
	    	if (!userData.availableEvolutions.contains("Demonoid")) {
	    	userData.availableEvolutions.add("Demonoid");}
	    }
	    if (userData.Cdata.get("Race").equals("Human (OtherWorlder)") && newlevel >30) {
	    	if (!userData.availableEvolutions.contains("High Human (OtherWorlder)")) {
	    	userData.availableEvolutions.add("High Human (OtherWorlder)");}
	    }
	    if (userData.Cdata.get("Race").equals("High Human (OtherWorlder)") && newlevel >80) {
	    	if (!userData.availableEvolutions.contains("Divine Human (OtherWorlder)")) {
	    	userData.availableEvolutions.add("Divine Human (OtherWorlder)");}
	    }
	    if ((userData.Cdata.get("Race").equals("Divine Human (OtherWorlder)") ||userData.Cdata.get("Race").equals("High Human (OtherWorlder)")) && newlevel >80 
	    		&& userData.Cdata.get("Alignment").contains("Chaotic") ||userData.Cdata.get("Alignment").contains("Evil")) {
	    	if (!userData.availableEvolutions.contains("Demonoid")) {
	    	userData.availableEvolutions.add("Demonoid");}
	    }
	    if (userData.Cdata.get("Race").equals("Witch") && newlevel >10 && userData.Cdata.get("Alignment").contains("Chaotic") ||
	    		userData.Cdata.get("Alignment").contains("Evil") || userData.Cdata.get("Alignment").contains("Neutral Neutral")) {
	    	if (!userData.availableEvolutions.contains("Evil Witch")) {
	    	userData.availableEvolutions.add("Evil Witch");}}
	    if (userData.Cdata.get("Race").equals("Evil Witch") && newlevel >50) {
		    	if (!userData.availableEvolutions.contains("Dark Affinity Witch")) {
		    	userData.availableEvolutions.add("Dark Affinity Witch");}
		    }
	    if (userData.Cdata.get("Race").equals("Witch") && newlevel >10 &&( userData.Cdata.get("Alignment").contains("Lawful") ||
	    		userData.Cdata.get("Alignment").contains("Good") || userData.Cdata.get("Alignment").contains("Neutral Neutral"))) {
	    	if (!userData.availableEvolutions.contains("Benevolent Witch")) {
	    	userData.availableEvolutions.add("Benevolent Witch");}}
	    if (userData.Cdata.get("Race").equals("Benevolent Witch") && newlevel >50) {
	    	if (!userData.availableEvolutions.contains("Light Affinity Witch")) {
	    	userData.availableEvolutions.add("Light Affinity Witch");}}
	    if (userData.Cdata.get("Race").equals("Benevolent Witch") && newlevel >50 && (userData.Cdata.get("Alignment").contains("Evil") || userData.Cdata.get("Alignment").contains("Chaotic"))) {
	    	if (!userData.availableEvolutions.contains("Chaos Affinity Witch")) {
	    	userData.availableEvolutions.add("Chaos Affinity Witch");}}
	    if (userData.Cdata.get("Race").equals("Evil Witch") && newlevel >50 && (userData.Cdata.get("Alignment").contains("Good") || userData.Cdata.get("Alignment").contains("Lawful"))) {
	    	if (!userData.availableEvolutions.contains("Chaos Affinity Witch")) {
	    	userData.availableEvolutions.add("Chaos Affinity Witch");}}
	    if (userData.Cdata.get("Race").equals("Sorceress") && newlevel >50) {
	    	if (!userData.availableEvolutions.contains("Primordial Sorceress")) {
	    	userData.availableEvolutions.add("Primordial Sorceress");}
	    }
	    if (userData.Cdata.get("Race").equals("Halfling") && newlevel >70) {
	    	if (!userData.availableEvolutions.contains("Archaic Halfling")) {
	    	userData.availableEvolutions.add("Archaic Halfling");}
	    }
	    if (userData.Cdata.get("Race").equals("Dwarf") && newlevel >25) {
	    	if (!userData.availableEvolutions.contains("High Dwarf")) {
	    	userData.availableEvolutions.add("High Dwarf");}
	    }
	    if (userData.Cdata.get("Race").equals("Elf") && newlevel >55) {
	    	if (!userData.availableEvolutions.contains("High Elf")) {
	    	userData.availableEvolutions.add("High Elf");}
	    }
	    if (userData.Cdata.get("Race").equals("Elf") && newlevel >40 && userData.Abilities.containsKey("Dark Magic")) {
	    	if (!userData.availableEvolutions.contains("Dark Elf")) {
	    	userData.availableEvolutions.add("Dark Elf");}
	    }
	    if (userData.Cdata.get("Race").equals("Merfolk") && newlevel >40) {
	    	if (!userData.availableEvolutions.contains("Siren")) {
	    	userData.availableEvolutions.add("Siren");}
	    }
	    if (userData.Cdata.get("Race").equals("Goblin") && newlevel >5) {
	    	if (!userData.availableEvolutions.contains("Goblina")) {
	    	userData.availableEvolutions.add("Goblina");}
	    }
	    if (userData.Cdata.get("Race").equals("Goblin") && newlevel >5) {
	    	if (!userData.availableEvolutions.contains("Hobgoblin")) {
	    	userData.availableEvolutions.add("Hobgoblin");}
	    }
	    if (userData.Cdata.get("Race").equals("Goblina") && newlevel >15) {
	    	if (!userData.availableEvolutions.contains("Ogre")) {
	    	userData.availableEvolutions.add("Ogre");}
	    }
	    if (userData.Cdata.get("Race").equals("HobGoblin") && newlevel >15) {
	    	if (!userData.availableEvolutions.contains("Ogre")) {
	    	userData.availableEvolutions.add("Ogre");}
	    }
	    if (userData.Cdata.get("Race").equals("HobGoblin") && newlevel >15) {
	    	if (!userData.availableEvolutions.contains("Ogre")) {
	    	userData.availableEvolutions.add("Ogre");}
	    }
	    if (userData.Cdata.get("Race").equals("Ogre") && newlevel >25) {
	    	if (!userData.availableEvolutions.contains("Kjinn")) {
	    	userData.availableEvolutions.add("Kjinn");}
	    }
	    if (userData.Cdata.get("Race").equals("Kjinn") && newlevel >40) {
	    	if (!userData.availableEvolutions.contains("Oni")) {
	    	userData.availableEvolutions.add("Oni");}
	    }
	    if (userData.Cdata.get("Race").equals("Kjinn") && newlevel >60 && userData.Abilities.containsKey("Fire Magic")) {
	    	if (!userData.availableEvolutions.contains("Enki")) {
	    	userData.availableEvolutions.add("Enki");}
	    }
	    if (userData.Cdata.get("Race").equals("Oni") && newlevel >60 && userData.Abilities.containsKey("Fire Magic") || userData.Abilities.containsKey("Water Magic") || userData.Abilities.containsKey("Earth Magic")|| userData.Abilities.containsKey("Wing Magic")) {
	    	if (!userData.availableEvolutions.contains("Spirit Oni")) {
	    	userData.availableEvolutions.add("Spirit Oni");}
	    }
	    if (userData.Cdata.get("Race").equals("Oni") && newlevel >60 && userData.Abilities.containsKey("Dark Magic")) {
	    	if (!userData.availableEvolutions.contains("Mystic Oni")) {
	    	userData.availableEvolutions.add("Mystic Oni");}
	    }
	    if (userData.Cdata.get("Race").equals("Oni") && newlevel >60 && userData.Abilities.containsKey("Light Magic")) {
	    	if (!userData.availableEvolutions.contains("Divine Oni")) {
	    	userData.availableEvolutions.add("Divine Oni");}
	    }
	    if (userData.Cdata.get("Race").equals("Spirit Oni") && newlevel >80) {
	    	if (!userData.availableEvolutions.contains("Death Oni")) {
	    	userData.availableEvolutions.add("Death Oni");}
	    }
	    if (userData.Cdata.get("Race").equals("Mystic Oni") && newlevel >80) {
	    	if (!userData.availableEvolutions.contains("Wicked Oni")) {
	    	userData.availableEvolutions.add("Wicked Oni");}
	    }
	    if (userData.Cdata.get("Race").equals("Divine Oni") && newlevel >80) {
	    	if (!userData.availableEvolutions.contains("Divine Fighter Oni")) {
	    	userData.availableEvolutions.add("Divine Fighter Oni");}
	    }
	    if (userData.Cdata.get("Race").equals("Lycanthrope") && newlevel >30) {
	    	if (!userData.availableEvolutions.contains("Sprit Beast")) {
	    	userData.availableEvolutions.add("Spirit Beast");}
	    }
	    if (userData.Cdata.get("Race").equals("Spirit Beast") && newlevel >60) {
	    	if (!userData.availableEvolutions.contains("Divine Beast")) {
	    	userData.availableEvolutions.add("Divine Beast");}
	    }
	    if (userData.Cdata.get("Race").equals("Tengu") && newlevel >35) {
	    	if (!userData.availableEvolutions.contains("Kamikemono")) {
	    	userData.availableEvolutions.add("Kamikemono");}
	    }
	    if (userData.Cdata.get("Race").equals("Rabbitfolk") && newlevel >80) {
	    	if (!userData.availableEvolutions.contains("Spirit Rabbit")) {
	    	userData.availableEvolutions.add("Spirit Rabbit");}
	    }
	    if (userData.Cdata.get("Race").equals("Kobold") && newlevel >60) {
	    	if (!userData.availableEvolutions.contains("True Chimera")) {
	    	userData.availableEvolutions.add("True Chimera");}
	    }
	    if (userData.Cdata.get("Race").equals("Kobold") && newlevel >30 && userData.Cdata.get("Alignment").contains("Chaotic")) {
	    	if (!userData.availableEvolutions.contains("Spirit Beast")) {
	    	userData.availableEvolutions.add("Spirit Beast");}
	    }
	    if (userData.Cdata.get("Race").equals("Orc") && newlevel >20) {
	    	if (!userData.availableEvolutions.contains("Orc Knight")) {
	    	userData.availableEvolutions.add("Orc Knight");}
	    }
	    if (userData.Cdata.get("Race").equals("Orc Knight") && newlevel >30) {
	    	if (!userData.availableEvolutions.contains("Orc General")) {
	    	userData.availableEvolutions.add("Orc General");}
	    }
	    if (userData.Cdata.get("Race").equals("Orc General") && newlevel >50) {
	    	if (!userData.availableEvolutions.contains("Orc Lord")) {
	    	userData.availableEvolutions.add("Orc Lord");}
	    }
	    if (userData.Cdata.get("Race").equals("Orc Knight") && newlevel >30 && userData.Cdata.get("Alignment").contains("Lawful")) {
	    	if (!userData.availableEvolutions.contains("High Orc")) {
	    	userData.availableEvolutions.add("High Orc");}
	    }
	    if (userData.Cdata.get("Race").equals("High Orc") && newlevel >50) {
	    	if (!userData.availableEvolutions.contains("Spirit Boar")) {
	    	userData.availableEvolutions.add("Spirit Boar");}
	    }
	    if (userData.Cdata.get("Race").equals("Spirit Boar") && newlevel >65) {
	    	if (!userData.availableEvolutions.contains("Divine Boar")) {
	    	userData.availableEvolutions.add("Divine Boar");}
	    }
	    if (userData.Cdata.get("Race").equals("Orc General") && newlevel >50) {
	    	if (!userData.availableEvolutions.contains("Orc Lord")) {
	    	userData.availableEvolutions.add("Orc Lord");}
	    }
	    if (userData.Cdata.get("Race").equals("Lizardmen") && newlevel >40) {
	    	if (!userData.availableEvolutions.contains("Dragonnewt")) {
	    	userData.availableEvolutions.add("Dragonnewt");}
	    }
	    if (userData.Cdata.get("Race").equals("Dragonnewt") && newlevel >70) {
	    	if (!userData.availableEvolutions.contains("True Dragonnewt")) {
	    	userData.availableEvolutions.add("True Dragonnewt");}
	    }
	    if (userData.Cdata.get("Race").equals("Spectre") && newlevel >20) {
	    	if (!userData.availableEvolutions.contains("Phantom")) {
	    	userData.availableEvolutions.add("Phantom");}
	    }
	    if (userData.Cdata.get("Race").equals("Phantom") && newlevel >40 && userData.stats.get("magic")>150 && userData.Cdata.get("Alignment").contains("Good")) {
	    	if (!userData.availableEvolutions.contains("Mystic Angel")) {
	    	userData.availableEvolutions.add("Mystic Angel");}
	    }
	    if (userData.Cdata.get("Race").equals("Phantom") && newlevel >40 && userData.stats.get("magic")>150) {
	    	if (!userData.availableEvolutions.contains("Arch Daemon")) {
	    	userData.availableEvolutions.add("Arch Daemon");}
	    }
	    if (userData.Cdata.get("Race").equals("Mystic Angel") && newlevel >75) {
	    	if (!userData.availableEvolutions.contains("True Angel")) {
	    	userData.availableEvolutions.add("True Angel");}
	    }
	    if (userData.Cdata.get("Race").equals("Lesser Daemon") && userData.stats.get("magic")>50) {
	    	if (!userData.availableEvolutions.contains("Greater Daemon")) {
	    	userData.availableEvolutions.add("Greater Daemon");}
	    }
	    if (userData.Cdata.get("Race").equals("Greater Daemon") && userData.stats.get("magic")>100) {
	    	if (!userData.availableEvolutions.contains("Arch Daemon")) {
	    	userData.availableEvolutions.add("Arch Daemon");}
	    }
	    if (userData.Cdata.get("Race").equals("Arch Daemon") && userData.stats.get("magic")>200) {
	    	if (!userData.availableEvolutions.contains("Daemon Lord")) {
	    	userData.availableEvolutions.add("Daemon Lord");}
	    }
	    if (userData.Cdata.get("Race").equals("Daemon Lord") && userData.stats.get("magic")>300) {
	    	if (!userData.availableEvolutions.contains("Devil Lord")) {
	    	userData.availableEvolutions.add("Devil Lord");}
	    }
	    if (userData.Cdata.get("Race").equals("Lesser Elemental Spirit") && newlevel >10) {
	    	if (!userData.availableEvolutions.contains("Medium Elemental Spirit")) {
	    	userData.availableEvolutions.add("Medium Elemental Spirit");}
	    }
	    if (userData.Cdata.get("Race").equals("Medium Elemental Spirit") && newlevel >20) {
	    	if (!userData.availableEvolutions.contains("Greater Elemental Spirit")) {
	    	userData.availableEvolutions.add("Greater Elemental Spirit");}
	    }
	    if (userData.Cdata.get("Race").equals("Greater Elemental Spirit") && newlevel >40) {
	    	if (!userData.availableEvolutions.contains("Elemental Lord")) {
	    	userData.availableEvolutions.add("Elemental Lord");}
	    }
	    if (userData.Cdata.get("Race").equals("Elemental Lord") && newlevel >55  &&userData.Cdata.get("Alignment").contains("Good")) {
	    	if (!userData.availableEvolutions.contains("Holy Elemental")) {
	    	userData.availableEvolutions.add("Holy Elemental");}
	    }
	    if (userData.Cdata.get("Race").equals("Holy Elemental") && newlevel >85) {
	    	if (!userData.availableEvolutions.contains("Elemental Royalty")) {
	    	userData.availableEvolutions.add("Elemental Royalty");}
	    }
	    if (userData.Cdata.get("Race").equals("Elemental Lord") && newlevel >70) {
	    	if (!userData.availableEvolutions.contains("Grand Elemental")) {
	    	userData.availableEvolutions.add("Grand Elemental");}
	    }
	    if (userData.Cdata.get("Race").equals("Grand Elemental") && newlevel >90) {
	    	if (!userData.availableEvolutions.contains("Daemon Lord")) {
	    	userData.availableEvolutions.add("Daemon Lord");}
	    }
	    if (userData.Cdata.get("Race").equals("Greater Elemental Spirit") && newlevel >30&& userData.Abilities.containsKey("Earth Magic") ) {
	    	if (!userData.availableEvolutions.contains("Lorelei")) {
	    	userData.availableEvolutions.add("Lorelei");}
	    }
	    if (userData.Cdata.get("Race").equals("Dryad") && newlevel >70) {
	    	if (!userData.availableEvolutions.contains("Dryas")) {
	    	userData.availableEvolutions.add("Dryas");}
	    }
	    if (userData.Cdata.get("Race").equals("Giant") && newlevel >50) {
	    	if (!userData.availableEvolutions.contains("True Giant")) {
	    	userData.availableEvolutions.add("True Giant");}
	    }
	    if (userData.Cdata.get("Race").equals("Giant") && newlevel >50) {
	    	if (!userData.availableEvolutions.contains("Cyclops")) {
	    	userData.availableEvolutions.add("Cyclops");}
	    }
	    if (userData.Cdata.get("Race").equals("Giant") && newlevel >40) {
	    	if (!userData.availableEvolutions.contains("Giant Ogre")) {
	    	userData.availableEvolutions.add("Giant Ogre");}
	    }
	    if (userData.Cdata.get("Race").equals("Giant Ogre") && newlevel >80) {
	    	if (!userData.availableEvolutions.contains("Hecatoncheires")) {
	    	userData.availableEvolutions.add("Hecatoncheires");}
	    }
	    if (userData.Cdata.get("Race").equals("Gozu") && newlevel >25) {
	    	if (!userData.availableEvolutions.contains("Gyuuki")) {
	    	userData.availableEvolutions.add("Gyuuki");}
	    }
	    if (userData.Cdata.get("Race").equals("Harpy") && newlevel >25) {
	    	if (!userData.availableEvolutions.contains("Spirit Bird")) {
	    	userData.availableEvolutions.add("Spirit Bird");}
	    }
	    if (userData.Cdata.get("Race").equals("Spirit Bird") && newlevel >60) {
	    	if (!userData.availableEvolutions.contains("Divine Bird")) {
	    	userData.availableEvolutions.add("Divine Bird");}
	    }
	    if (userData.Cdata.get("Race").equals("Mezu") && newlevel >35) {
	    	if (!userData.availableEvolutions.contains("Baki")) {
	    	userData.availableEvolutions.add("Baki");}
	    }
	    if (userData.Cdata.get("Race").equals("Wight") && newlevel >10) {
	    	if (!userData.availableEvolutions.contains("Wight King")) {
	    	userData.availableEvolutions.add("Wight King");}
	    }
	    if (userData.Cdata.get("Race").equals("Wight King") && newlevel >35) {
	    	if (!userData.availableEvolutions.contains("Lich")) {
	    	userData.availableEvolutions.add("Lich");}
	    }
	    if (userData.Cdata.get("Race").equals("Lich") && newlevel >66) {
	    	if (!userData.availableEvolutions.contains("True Lich")) {
	    	userData.availableEvolutions.add("True Lich");}
	    }
	    if (userData.Cdata.get("Race").equals("Insectar") && newlevel >30) {
	    	if (!userData.availableEvolutions.contains("Divine Insect")) {
	    	userData.availableEvolutions.add("Divine Insect");}
	    }
	    if (userData.Cdata.get("Race").equals("Divine Insect") && newlevel >80) {
	    	if (!userData.availableEvolutions.contains("Star Construct Wasp")) {
	    	userData.availableEvolutions.add("Star Construct Wasp");}
	    }
	    if (userData.Cdata.get("Race").equals("Spiritual Doll") && newlevel >60) {
	    	if (!userData.availableEvolutions.contains("Chaos Doll")) {
	    	userData.availableEvolutions.add("Chaos Doll");}
	    }
	    if (userData.Cdata.get("Race").equals("Chaos Doll") && newlevel >80) {
	    	if (!userData.availableEvolutions.contains("Chaos Metalloid")) {
	    	userData.availableEvolutions.add("Chaos Metalloid");}
	    }
	    if (userData.Cdata.get("Race").equals("Chaos Metalloid") && newlevel >90) {
	    	if (!userData.availableEvolutions.contains("Arch Doll")) {
	    	userData.availableEvolutions.add("Arch Doll");}
	    }
	    if (userData.Cdata.get("Race").equals("Dhampir") && newlevel >34) {
	    	if (!userData.availableEvolutions.contains("Vampire")) {
	    	userData.availableEvolutions.add("Vampire");}
	    }
	    if (userData.Cdata.get("Race").equals("Vampire") && newlevel >69) {
	    	if (!userData.availableEvolutions.contains("Surmounter")) {
	    	userData.availableEvolutions.add("Surmounter");}
	    }
	    if (userData.availableEvolutions.size()>0) {
	    	event.getChannel().sendMessage("You have Evolutions Available").queue();
	    }
	    saveUserData();
}
private void evolutionCheck(Member member, MessageReceivedEvent event) {
	if (member == null) {
		member = event.getMember();
	}
	String userId = member.getId();
    UserData userData = userDataMap.get(userId);
    if (userData.Proxy) {
    	userId = userData.ProxyUserID;
    }

    userData = userDataMap.get(userId);
    if (userData.Cdata == null) {
	    // Handle the situation where userData or Cdata is null
	    // For example, initialize Cdata explicitly if it can be null
	    userData.Cdata = new LinkedHashMap<>();}
    if (userData.stats == null) {
	    // Handle the situation where userData or Cdata is null
	    // For example, initialize Cdata explicitly if it can be null
	    userData.stats = new HashMap<>();}
    if (userData.Abilities == null) {
	    // Handle the situation where userData or Cdata is null
	    // For example, initialize Cdata explicitly if it can be null
	    userData.Abilities = new LinkedHashMap<>();}
    if (userData.availableEvolutions == null) {
	    // Handle the situation where userData or Cdata is null
	    // For example, initialize Cdata explicitly if it can be null
	    userData.availableEvolutions = new ArrayList<>();}
    
    if (userData.availableEvolutions.size()>0) {
    	StringBuilder MessageText = new StringBuilder();
    MessageText.append("You have " + userData.availableEvolutions.size()+ " Available!" + "\n");
    for (String evolution : userData.availableEvolutions) {
    	MessageText.append(evolution + "\n");
    }
    List<Button> buttons = ButtonClickListener.createButtons(userData.availableEvolutions);
    event.getChannel().sendMessage(MessageText.toString()).setActionRow(buttons).queue();
    } else 
    event.getChannel().sendMessage("You have no available Evolutions");
    saveUserData();
}
public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
    String buttonId = event.getButton().getId();
    if (buttonId == null) return;
    Member member = event.getMember();
    String userId = member.getId();
    UserData userData = userDataMap.get(userId);
    if (userData.Proxy) {
    	userId = userData.ProxyUserID;
    }

    userData = userDataMap.get(userId);

    for (int i = 0; i < userData.availableEvolutions.size(); i++) {
        if (buttonId.equals(userData.availableEvolutions.get(i))) {
            event.reply("You are now " + userData.availableEvolutions.get(i) + " race!").queue();
            userData.Cdata.put("Race", userData.availableEvolutions.get(i));
            userData.availableEvolutions.clear();
            break;
        }
    }
}

public void ProxyCheck(String yesOrNo, MessageReceivedEvent event, Member targetMember) {
	if (targetMember == null) {
		targetMember = event.getMember();
	}
	yesOrNo=yesOrNo.toLowerCase();
	Member member = event.getMember();
    String userId = member.getId();
    userDataMap.putIfAbsent(userId, new UserData(userId));
    UserData userData = userDataMap.get(userId);
	if (yesOrNo.equals("yes") || yesOrNo.equals("on")) {
		if (userData.ProxyUserID == "") {
			userData.ProxyUserID = String.valueOf(roll(1000000000,event));
		}
		event.getChannel().sendMessage("You are now using a Proxy").queue();
		userData.Proxy=true;
	} else if (yesOrNo.equals("no") || yesOrNo.equals("off")){userData.Proxy = false;
	event.getChannel().sendMessage("Proxy has been turned off").queue();}
	else event.getChannel().sendMessage("Please use Yes, On, Off, or No").queue();
	 saveUserData();
}
public void imageRetrieval(MessageReceivedEvent event) {
    String url = "";
    Member member = event.getMember();
    String userId = member.getId();
    UserData userData = userDataMap.get(userId);
    if (userData.Proxy) {
        userId = userData.ProxyUserID;
    }
    userData = userDataMap.get(userId);

    if (event.getAuthor().isBot()) return;
    List<Message.Attachment> attachments = event.getMessage().getAttachments();

    if (attachments.isEmpty()) return; // No attachments
    Message.Attachment attachment = attachments.get(0); // Assuming there's only one attachment
    if (!attachment.isImage()) return; // Ensure it's an image attachment

    try {
        url = new URL(attachment.getUrl()).toString();
    } catch (MalformedURLException e) {
    	event.getChannel().sendMessage("Image is incompatible, please try again").queue();
    }
    userData.Avatar = resize(event, url);
    if (userData.Avatar.equals("")) {
    	return;
    }
    event.getChannel().sendMessage("Image saved").queue();
    saveUserData();
}

public static InputStream stringToInputStream(String data) {
    return new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
}
public static String inputStreamToString(InputStream inputStream) throws IOException {
    ByteArrayOutputStream result = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024];
    int length;
    while ((length = inputStream.read(buffer)) != -1) {
        result.write(buffer, 0, length);
    }
    return result.toString(StandardCharsets.UTF_8.name());
}
private InputStream downloadImage(String dataUrl) throws IOException {
    if (dataUrl.startsWith("data:image/")) {
        String base64Image = dataUrl.split(",")[1];
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        return new ByteArrayInputStream(imageBytes);
    } else {
        URL url = new URL(dataUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        return connection.getInputStream();
    }
}
public String resize(MessageReceivedEvent Event, String URL) {
	String DataUrl = "";
	 Member member = Event.getMember();
	    String userId = member.getId();
	    UserData userData = userDataMap.get(userId);
	    if (userData.Proxy) {
	    	userId = userData.ProxyUserID;
	    }
	    userData = userDataMap.get(userId);

	  try {
	
	 URL url = new URL(URL);
  HttpURLConnection connection = (HttpURLConnection) url.openConnection();
  connection.setRequestMethod("GET");

  // Read image data from connection
  InputStream inputStream = connection.getInputStream();
  ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  byte[] buffer = new byte[4096];
  int bytesRead;
  while ((bytesRead = inputStream.read(buffer)) != -1) {
      outputStream.write(buffer, 0, bytesRead);
  }

  // Convert image data to byte array
  byte[] imageData = outputStream.toByteArray();
  ByteArrayOutputStream baos = new ByteArrayOutputStream();
  ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
  BufferedImage bufferedImage = ImageIO.read(bis);
  if (bufferedImage == null) {
	  Event.getChannel().sendMessage("Image is incompatible, please try again").queue();
	  String Null = "";
	  return Null;
  }
  
  int targetSize = 250;
  int centerX = bufferedImage.getWidth() / 2;
  int centerY = bufferedImage.getHeight() / 2;

  // Determine the size of the square to crop
  int cropSize = Math.min(bufferedImage.getWidth(), bufferedImage.getHeight());

  // Calculate crop bounds to center the square
  int cropX = centerX - (cropSize / 2);
  int cropY = centerY - (cropSize / 2);

  // Ensure crop bounds are within image dimensions
  cropX = Math.max(0, cropX);
  cropY = Math.max(0, cropY);

  // Create a new BufferedImage for the cropped image
  BufferedImage croppedImage = new BufferedImage(cropSize, cropSize, BufferedImage.TYPE_INT_RGB);
  Graphics2D g = croppedImage.createGraphics();
  g.drawImage(bufferedImage, 0, 0, cropSize, cropSize, cropX, cropY, cropX + cropSize, cropY + cropSize, null);
  g.dispose();

  // Resize the cropped image to the target size
  BufferedImage resizedImage = new BufferedImage(targetSize, targetSize, BufferedImage.TYPE_INT_RGB);
  g = resizedImage.createGraphics();
  g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
  g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
  g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
  g.drawImage(croppedImage, 0, 0, targetSize, targetSize, null);
  g.dispose();


  ImageIO.write(resizedImage, "jpg", baos );
  byte[] imageData2 = baos.toByteArray();
  String base64Image = Base64.getEncoder().encodeToString(imageData2);

  // Construct the Data URL
  DataUrl = "data:image/png;base64," + base64Image;
  
  // Obtain URL of the saved file

  outputStream.close();
 
		inputStream.close();
	
  connection.disconnect();
  

//catch () {
}catch (IOException e) {
		e.printStackTrace();
	}
return DataUrl;
}
public void IDCardCrafting(Member member, MessageReceivedEvent event, String color){
    String userId = member.getId();
    UserData userData = userDataMap.get(userId);
    if (userData != null && userData.Proxy) {
        userId = userData.ProxyUserID;
    }
    userData = userDataMap.get(userId);

    if (userData == null) {
        event.getChannel().sendMessage("User data not found.").queue();
        return;
    }
    if (userData.Cdata == null) {
	    // Handle the situation where userData or Cdata is null
	    // For example, initialize Cdata explicitly if it can be null
	    userData.Cdata = new LinkedHashMap<>();}
    if (userData.stats == null) {
	    // Handle the situation where userData or Cdata is null
	    // For example, initialize Cdata explicitly if it can be null
	    userData.stats = new HashMap<>();}
    if (userData.Abilities == null) {
	    // Handle the situation where userData or Cdata is null
	    // For example, initialize Cdata explicitly if it can be null
	    userData.Abilities = new LinkedHashMap<>();}
    if (userData.availableEvolutions == null) {
	    // Handle the situation where userData or Cdata is null
	    // For example, initialize Cdata explicitly if it can be null
	    userData.availableEvolutions = new ArrayList<>();}
    if (userData.Armor == null) {
    	 userData.Armor = new LinkedHashMap<>();
    }
    if (userData.Weapon == null) {
   	 userData.Weapon = new LinkedHashMap<>();
   }
    if (userData.Enchant == null) {
   	 userData.Enchant = new LinkedHashMap<>();
   }
    
    updateLeftover(userData);
  	updateVitality(userData, 0, 0);
int x = 400;
int y = 400;
    InputStream inputStream;
	try {
		inputStream = downloadImage("data:image/png;base64,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"
				+ "");
	
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    byte[] buffer = new byte[4096];
    int bytesRead;
    while ((bytesRead = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
    }

    // Convert image data to byte array
    byte[] imageData = outputStream.toByteArray();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
    BufferedImage additionalImage = ImageIO.read(bis);
    InputStream inputStream2 = downloadImage("data:image/png;base64,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");

    BufferedImage image2 = ImageIO.read(inputStream2);
   
    Graphics2D g2d = additionalImage.createGraphics();
	// Y coordinate where the image will be drawn
   float opacity;
    opacity = 1.0f;
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,opacity));
   g2d.drawImage(additionalImage, 0, 0, null);
   Color backgroundColor;
    if(!color.equals(""))
    		{backgroundColor = Color.decode(color);
    
    		}else {
    			 if (!userData.Avatar.equals("")) {
    				    InputStream inputStream3 = downloadImage(userData.Avatar);
    				  
    				    BufferedImage image3 = ImageIO.read(inputStream3);
    backgroundColor=getMostFrequentBorderColor(image3);}
    			 else backgroundColor = Color.BLACK;}
    g2d.setColor(backgroundColor);
    opacity = 0.5f;
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,opacity));
    g2d.fillRect(0, 0, 1000, 1000);
    opacity = 1.0f;
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,opacity));
    g2d.drawImage(image2, 0, 0, null);
    Color darker2 = decreaseBrightness(backgroundColor, .5);
    g2d.setColor(darker2);
    g2d.fillRect(258, 30, 264, 264);
    if (!userData.Avatar.equals("")) {
	    InputStream inputStream3 = downloadImage(userData.Avatar);
	  
	    BufferedImage image3 = ImageIO.read(inputStream3);
    g2d.drawImage(image3, 265, 37, null);}
    int Levelish = (int)userData.getStat("Level")+1;
  	StringBuilder Abilities = new StringBuilder();
  	String raceName = userData.getCdata("Race");
  	String Name = userData.getCdata("Character Name");
  	Entry<String, String> Armor = getEntryByIndex(userData.Armor,0);
  
	  	Entry<String, String> Weapon = getEntryByIndex(userData.Weapon,0);
	  	Entry<String, String> Ring = getEntryByIndex(userData.Enchant,0);
  	for (int i = 0; i < userData.Abilities.size(); i++) {
  		if (i <12) {
  			 Map.Entry<String, String> entry = getEntryByIndex(userData.Abilities, i);
  	  	
  		
  		 if (entry.getKey().length()<25) {
  		Abilities.append(entry.getKey()+ " "+"\n");
  		} else Abilities.append("Ability name is too long");
  		}	}
  	if (raceName.length()<2) {
  		raceName = "None";
  	}String ArmorName;
  	String ArmorValue;
  	if (Armor!=null) {
  		if (Armor.getKey()!=null||!Armor.getKey().equals("")) {
  				ArmorName = Armor.getKey();
  		} else ArmorName = "None";
  		if (Armor.getValue()==null||Armor.getValue().equals("")) {
  	ArmorValue = Armor.getValue();}
  		else ArmorValue = "+0";
  	
  	} else {ArmorName = "None";
  	ArmorValue = "+0";}
  	String WeaponName;
  		String WeaponValue;
  		if (Weapon!=null) {
  			if (Weapon.getKey()!=null||!Weapon.getKey().equals("")) {
  				WeaponName = Weapon.getKey();
  		} else WeaponName = "None";
  		if (Weapon.getValue()==null||Weapon.getValue().equals("")) {
  	WeaponValue = Weapon.getValue();}
  		else WeaponValue = "+0";
  		  	} else {WeaponName = "None";
  		  	WeaponValue = "+0";}
  	String RingName;
  	  	String RingValue;
  	  if (Ring !=null) {
  		if (Ring.getKey()!=null||!Ring.getKey().equals("")) {
				RingName = Ring.getKey();
		} else RingName = "None";
		if (Ring.getValue()==null||Ring.getValue().equals("")) {
	RingValue = Ring.getValue();}
		else RingValue = "+0";
  	  	} else {RingName = "None";
  	  	RingValue = "+0";}
  	if (Name == null) {
  		if (!raceName.equals("None")) {
  		Name = "Unnamed " + raceName + "\n";} else Name = "";
  	} else Name = userData.getCdata("Character Name") + "\n";
  	if (ArmorName.length()>15) {
  		List<String> lines = splitString(ArmorName, 15);
  			String Holder = "";
  		for (String line : lines) {
  			if (lines.get(0)==line) {
  	  			Holder = Holder + line;
  	  		} else
  			Holder = Holder +  "\n" + line;
  			
  		}
  		ArmorName = Holder;
  	}
	if (WeaponName.length()>15) {
  		List<String> lines = splitString(WeaponName, 15);
  			String Holder = "";
  		for (String line : lines) {
  			if (lines.get(0)==line) {
  	  			Holder = Holder + line;
  	  		} else
  			Holder = Holder + "\n" + line;
  			
  		}
  		WeaponName = Holder;
  	}
	if (RingName.length()>15) {
  		List<String> lines = splitString(RingName, 15);
  			String Holder = "";
  		for (String line : lines) {
  			if (lines.get(0)==line) {
  	  			Holder = Holder + line;
  	  		} else
  			Holder = Holder + "\n" + line;
  			
  		}
  		RingName = Holder;
  	}
	if (Name.length()>20) {
  		List<String> lines = splitString(Name, 20);
  			String Holder = "";
  		for (String line : lines) {
  		if (lines.get(0)==line) {
  			Holder = Holder + line;
  		} else if (lines.get(lines.size()-1)==line) {
  			Holder = Holder + "\n"+ line + "\n";
  		}else
  			Holder = Holder + "\n"+ line;
  			
  		}
  		Name = Holder;
  	}
  	String text;
  	System.out.println();
    
	if(userData.getStat("Level")==0) {
  		text=	Name+ 
  				raceName + "\n" +
  				userData.Cdata.get("Alignment") +  "\n" +  "\n" +
  				"Unleveled" + "\n" +
  				"Total EXP: " + userData.getStat("EXP") + "\n" +
  				"EXP: " + userData.getStat("LeftoverEXP")+"/"+Levelish+ "\n"+ 
  				 "Remaining Points: " + userData.getStat("Remaining Points")  + "\n" + "\n" +
  				"Health(HP): " + userData.getStat("HP")+"/"+userData.getStat("Health") + "\n" +
  				"Vitality: " + userData.getStat("vitality") + "\n" +
  				"Strength: " + userData.getStat("strength") + "\n" +
                  "Dexterity: " + userData.getStat("dexterity") + "\n" +
                  "Agility: " + userData.getStat("agility") + "\n" +
                  "Endurance: " + userData.getStat("endurance") + "\n" +
                  "Willpower: " + userData.getStat("willpower") + "\n" +
                  "Magic: " + userData.getStat("magic") + "\n" +
                  "\n" +
                  
                  "Current Abilities" + "\n" + "\n" + Abilities
                  + "\n" + "\n" +
                  ArmorName + "\n" + ArmorValue  + "\n" + "\n"+
                  WeaponName + "\n" + WeaponValue  + "\n" +"\n" +
                  RingName + "\n" + RingValue
                  
                  		;} else text =	Name+
                  				raceName + "\n" +
                  				userData.Cdata.get("Alignment") +  "\n" +  "\n" +
                  				"Level: " + userData.getStat("level")+ "\n" +
                  				"Total EXP: " + userData.getStat("EXP") + "\n" +
                  				"EXP: " + userData.getStat("LeftoverEXP")+"/"+Levelish+ "\n"+ 
                  				 "Remaining Points: " + userData.getStat("Remaining Points")  + "\n" + "\n" +
                  				"Health(HP): " + userData.getStat("HP")+"/"+userData.getStat("Health") + "\n" +
                  				"Vitality: " + userData.getStat("vitality") + "\n" +
                  				"Strength: " + userData.getStat("strength") + "\n" +
                                  "Dexterity: " + userData.getStat("dexterity") + "\n" +
                                  "Agility: " + userData.getStat("agility") + "\n" +
                                  "Endurance: " + userData.getStat("endurance") + "\n" +
                                  "Willpower: " + userData.getStat("willpower") + "\n" +
                                  "Magic: " + userData.getStat("magic") + "\n" +
                                  "\n" +
                                  
                                  "Current Abilities" + "\n" + "\n" + Abilities
                                  + "\n" + "\n" +
                                  ArmorName + "\n" + ArmorValue  + "\n" + "\n"+
                                  WeaponName + "\n" + WeaponValue  + "\n" +"\n" +
                                  RingName + "\n" + RingValue
              
              		;
	 String[] Sections = text.split("\n" + "\n"); 
     // Draw each line of text
     int lineHeight;
     g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
     g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
     g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
     g2d.setFont(new Font("Lucida Handwriting", Font.BOLD, 12)); 
     x=55;
     y=55;
     Color darker = decreaseBrightness(backgroundColor, .2);
     g2d.setColor(darker);
     for (String section : Sections) {
     String[] lines = section.split("\n");
     for (String line : lines) {
   	  lineHeight = g2d.getFontMetrics().getHeight();
   	g2d.drawString(line, x, y);
         y += lineHeight;
     }
     if (section.equals(Sections[0])) {
    	 darker = decreaseBrightness(backgroundColor, 2);
         g2d.setColor(darker);
   	  //levels
   	y = 55; 
   	x = 565;
   	g2d.setFont(new Font("Lucida Handwriting", Font.BOLD, 15)); 
     }
     if (section.equals(Sections[1])) {
      	  //levels
    	 darker = decreaseBrightness(backgroundColor, .3);
         g2d.setColor(darker);
      	y = 130; 
      	x = 53;
      	g2d.setFont(new Font("Lucida Handwriting", Font.BOLD, 14)); 
        }
     if (section.equals(Sections[2])) {
    	 darker = decreaseBrightness(backgroundColor, 2);
         g2d.setColor(darker);
     	  //levels
     	y = 175; 
     	x = 565;
     	g2d.setFont(new Font("Lucida Handwriting", Font.BOLD, 15)); 
       }
     if (section.equals(Sections[3])) {
    	
    	g2d.setFont(new Font("Lucida Handwriting", Font.PLAIN, 12)); 
      }
     if (section.equals(Sections[4])) {
    	 darker = decreaseBrightness(backgroundColor, .3);
         g2d.setColor(darker);
    		y = 395; 
         	x = 230;
     	
     	g2d.setFont(new Font("Lucida Handwriting", Font.PLAIN, 12)); 
       }

     if (section.equals(Sections[5])) {
   		y = 395; 
        	x = 55;
    	
    	g2d.setFont(new Font("Lucida Handwriting", Font.PLAIN, 12)); 
      }

   
      
     if (section.equals(Sections[6])) {
   		y = 395; 
        	x = 390;
    	
    	g2d.setFont(new Font("Lucida Handwriting", Font.PLAIN, 12)); 
      }
     }
    
   // g2d.drawString(text, x, y);
    // Dispose the Graphics2D object
   
   

    // Read image data from connection
    
  
    g2d.dispose();
    try {
        ImageIO.write(additionalImage, "png", baos);
    } catch (IOException e) {
        e.printStackTrace();
        event.getChannel().sendMessage("Failed to create image.").queue();
        return;
    }

    imageData = baos.toByteArray();
    ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
    try {
        event.getChannel().sendMessage("").addFiles(FileUpload.fromData(bais, "image.png")).queue();
    } catch (Exception e) {
        e.printStackTrace();
        event.getChannel().sendMessage("Failed to send image.").queue();
    }} catch (IOException e) {
		e.printStackTrace();
	}
    }
public void IDCard1(MessageReceivedEvent event, Member member) {
	 String userId = member.getId();
	    UserData userData = userDataMap.get(userId);
	    if (userData != null && userData.Proxy) {
	        userId = userData.ProxyUserID;
	    }
	    userData = userDataMap.get(userId);

	    if (userData == null) {
	        event.getChannel().sendMessage("User data not found.").queue();
	        return;
	    }
	     
	    InputStream inputStream;
		try {
					+ "");
		
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        // Convert image data to byte array
        byte[] imageData = outputStream.toByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
        BufferedImage additionalImage = ImageIO.read(bis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // You can now manipulate the BufferedImage using Graphics2D
        Graphics2D g2d = additionalImage.createGraphics();
	    Color backgroundColor = Color.decode("#5E3830");
	    backgroundColor=Color.decode("#B0B0B0");
	    g2d.setColor(backgroundColor);
	    g2d.setColor(Color.DARK_GRAY);

	    
	    // Set the font and color for the text
	    g2d.setColor(Color.LIGHT_GRAY);

	    
	  
	    //float opacity = 0.25f;
	   //g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,opacity));
	 
	  String text;
	  updateLeftover(userData);
  	updateVitality(userData, 0, 0);
  	 if (userData == null || userData.Abilities == null) {
		    userData.Abilities = new LinkedHashMap<>();}
  	int Levelish = (int)userData.getStat("Level")+1;
  	StringBuilder Abilities = new StringBuilder();
  	String raceName = userData.getCdata("Race");
  	String Name = userData.getCdata("Character Name");
  	if (raceName == null) {
  		raceName = "none";
  	}
  	if (Name == null) {
  		if (!raceName.equals("None")) {
  		Name = "Unnamed " + raceName + "\n"+"\n";} else Name = "";
  	} else Name = userData.getCdata("Character Name") + "\n"+ "\n";
  	for (int i = 0; i < userData.Abilities.size(); i++) {
  		if (i <12) {
  			
  		 Map.Entry<String, String> entry = getEntryByIndex(userData.Abilities, i);
  		 if (entry.getKey().length()<100) {
  		Abilities.append(entry.getKey()+ " "+"\n");
  		} else Abilities.append("Ability is too long");
  		}	}
  	if(userData.getStat("Level")==0) {
  		text=	Name+
  				"Race: " + raceName + "\n" +"\n" +
  				"Alignment: " + userData.Cdata.get("Alignment") +  "\n" +  "\n" +
  				"Unleveled" + "\n" +
  				"Total EXP: " + userData.getStat("EXP") + "\n" +
  				"EXP: " + userData.getStat("LeftoverEXP")+"/"+Levelish+ "\n"+ 
  				 "Remaining Points: " + userData.getStat("Remaining Points")  + "\n" + "\n" +
  				"Health(HP): " + userData.getStat("HP")+"/"+userData.getStat("Health") + "\n" +
  				"Vitality: " + userData.getStat("vitality") + "\n" +
  				"Strength: " + userData.getStat("strength") + "\n" +
                  "Dexterity: " + userData.getStat("dexterity") + "\n" +
                  "Agility: " + userData.getStat("agility") + "\n" +
                  "Endurance: " + userData.getStat("endurance") + "\n" +
                  "Willpower: " + userData.getStat("willpower") + "\n" +
                  "Magic: " + userData.getStat("magic") + "\n" +
                  "\n" +
                  
                  "Current Abilities" +"\n"+"\n" + Abilities
                  		;} else text =	Name+
                  		"Race: " + raceName + "\n" +
  			"Alignment: " + userData.Cdata.get("Alignment") +  "\n" + "\n" +
				"Level: " + userData.getStat("Level") + "\n" +
				"Total EXP: " + userData.getStat("EXP") + "\n" +
				"EXP: " + userData.getStat("LeftoverEXP")+"/"+Levelish+ "\n"+ 
				 "Remaining Points: " + userData.getStat("Remaining Points")  + "\n" + "\n" +
				"Health(HP): " + userData.getStat("HP")+"/"+userData.getStat("Health") + "\n" +
				"Vitality: " + userData.getStat("vitality") + "\n" +
				"Strength: " + userData.getStat("strength") + "\n" +
              "Dexterity: " + userData.getStat("dexterity") + "\n" +
              "Agility: " + userData.getStat("agility") + "\n" +
              "Endurance: " + userData.getStat("endurance") + "\n" +
              "Willpower: " + userData.getStat("willpower") + "\n" +
              "Magic: " + userData.getStat("magic") + "\n" +
              "\n" +
            
              "Current Abilities" +"\n"+"\n" + Abilities
              		;
  	int xCenter = 700 / 2;
  	
 g2d.setFont(new Font("Lucida Handwriting", Font.BOLD, 30)); 
    // Calculate the x position for center alignment
    FontMetrics fm = g2d.getFontMetrics();
   
  	
	  String[] Sections = text.split("\n" + "\n"); 
	  int textWidth = fm.stringWidth(Sections[0]);
    int xPosition = xCenter - (textWidth / 2);
      // Draw each line of text
      int x = xPosition;  // X-coordinate of the text
      int y = 95;  // Starting Y-coordinate of the text
      int lineHeight;
      g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
      g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      for (String section : Sections) {
      String[] lines = section.split("\n");
      for (String line : lines) {
    	  lineHeight = g2d.getFontMetrics().getHeight();
          g2d.drawString(line, x, y);
          y += lineHeight;
      }
      if (section.equals(Sections[2])) {
    	  // levels
    	y = 250; 
    	x = 80;
    	g2d.setFont(new Font("Lucida Handwriting", Font.BOLD, 15)); 
      }
      if (section.equals(Sections[3])) {
    	  // stats
    	  x=290;  
      	y = 250;
      	g2d.setFont(new Font("Lucida Handwriting", Font.BOLD, 15)); 
        }
      if (section.equals(Sections[4])) {
    	  // abilities
        	
        	x=475;  
          	y = 250;
        	g2d.setFont(new Font("Lucida Handwriting", Font.BOLD, 15)); 
          }      if (section.equals(Sections[5])) {
        	  // abilities
      
          	g2d.setFont(new Font("Lucida Handwriting", Font.BOLD, 12)); 
          }   if (section.equals(Sections[0])) {
    	  //race
      	x=90;  
      	y = 140;
      	g2d.setFont(new Font("Lucida Handwriting", Font.BOLD, 18)); 
        }
      if (section.equals(Sections[1])) {
    	  //alignment
      	x=400;  
      	y = 140;
      	g2d.setFont(new Font("Lucida Handwriting", Font.BOLD, 18)); 
        }
      }
	     
	    //opacity = 1.0f;
	    //g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,opacity));
      g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
      g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	   g2d.drawImage(additionalImage, additionalImage.getWidth(), additionalImage.getHeight(), null);
	  
	    g2d.dispose();
	    try {
	        ImageIO.write(additionalImage, "png", baos);
	    } catch (IOException e) {
	        e.printStackTrace();
	        event.getChannel().sendMessage("Failed to create image.").queue();
	        return;
	    }

	    imageData = baos.toByteArray();
	    ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
	    try {
	        event.getChannel().sendMessage("").addFiles(FileUpload.fromData(bais, "image.png")).queue();
	    } catch (Exception e) {
	        e.printStackTrace();
	        event.getChannel().sendMessage("Failed to send image.").queue();
	    }} catch (IOException e) {
			e.printStackTrace();
		}
	    }
public void IDCard2(MessageReceivedEvent event, Member member) {
	 String userId = member.getId();
	    UserData userData = userDataMap.get(userId);
	    if (userData != null && userData.Proxy) {
	        userId = userData.ProxyUserID;
	    }
	    userData = userDataMap.get(userId);

	    if (userData == null) {
	        event.getChannel().sendMessage("User data not found.").queue();
	        return;
	    }
	     
	    InputStream inputStream;
		try {
					);
		
       
       ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
       byte[] buffer = new byte[4096];
       int bytesRead;
       while ((bytesRead = inputStream.read(buffer)) != -1) {
           outputStream.write(buffer, 0, bytesRead);
       }

       // Convert image data to byte array
       byte[] imageData = outputStream.toByteArray();
       ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
       BufferedImage additionalImage = ImageIO.read(bis);
       ByteArrayOutputStream baos = new ByteArrayOutputStream();
       // You can now manipulate the BufferedImage using Graphics2D
       Graphics2D g2d = additionalImage.createGraphics();
	    Color backgroundColor = Color.decode("#5E3830");
	    backgroundColor=Color.decode("#B0B0B0");
	    g2d.setColor(backgroundColor);
	    g2d.setColor(Color.DARK_GRAY);

	    
	    // Set the font and color for the text
	    g2d.setColor(Color.LIGHT_GRAY);

	    
	  
	    //float opacity = 0.25f;
	   //g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,opacity));
	 
	  String text;
	  updateLeftover(userData);
 	updateVitality(userData, 0, 0);
 	 if (userData == null || userData.Abilities == null) {
		    userData.Abilities = new LinkedHashMap<>();}
 	int Levelish = (int)userData.getStat("Level")+1;
 	StringBuilder Abilities = new StringBuilder();
 	String raceName = userData.getCdata("Race");
 	String Name = userData.getCdata("Character Name");
 	if (raceName == null) {
 		raceName = "none";
 	}
 	if (Name == null) {
 		if (!raceName.equals("None")) {
 		Name = "Unnamed " + raceName + "\n"+"\n";} else Name = "";
 	} else Name = userData.getCdata("Character Name") + "\n"+ "\n";
 	for (int i = 0; i < userData.Abilities.size(); i++) {
 		if (i <12) {
 			
 		 Map.Entry<String, String> entry = getEntryByIndex(userData.Abilities, i);
 		 if (entry.getKey().length()<100) {
 		Abilities.append(entry.getKey()+ " "+"\n");
 		} else Abilities.append("Ability is too long");
 		}	}
 	if(userData.getStat("Level")==0) {
 		text=	Name+
 				"Race: " + raceName + "\n" +"\n" +
 				"Alignment: " + userData.Cdata.get("Alignment") +  "\n" +  "\n" +
 				"Unleveled" + "\n" +
 				"Total EXP: " + userData.getStat("EXP") + "\n" +
 				"EXP: " + userData.getStat("LeftoverEXP")+"/"+Levelish+ "\n"+ 
 				 "Remaining" + "\n"+ "Points: " + userData.getStat("Remaining Points")  + "\n" + "\n" +
 				"Health(HP): " + userData.getStat("HP")+"/"+userData.getStat("Health") + "\n" +
 				"Vitality: " + userData.getStat("vitality") + "\n" +
 				"Strength: " + userData.getStat("strength") + "\n" +
                 "Dexterity: " + userData.getStat("dexterity") + "\n" +
                 "Agility: " + userData.getStat("agility") + "\n" +
                 "Endurance: " + userData.getStat("endurance") + "\n" +
                 "Willpower: " + userData.getStat("willpower") + "\n" +
                 "Magic: " + userData.getStat("magic") + "\n" +
                 "\n" 
                 		;} else text =	Name+
                 				"Race: " + raceName + "\n" +"\n" +
                 				"Alignment: " + userData.Cdata.get("Alignment") +  "\n" +  "\n" +
                 				"Level: " + userData.getStat("Level") + "\n" +
                 				"Total EXP: " + userData.getStat("EXP") + "\n" +
                 				"EXP: " + userData.getStat("LeftoverEXP")+"/"+Levelish+ "\n"+ 
                 				 "Remaining" + "\n"+ "Points: " + userData.getStat("Remaining Points")  + "\n" + "\n" +
                 				"Health(HP): " + userData.getStat("HP")+"/"+userData.getStat("Health") + "\n" +
                 				"Vitality: " + userData.getStat("vitality") + "\n" +
                 				"Strength: " + userData.getStat("strength") + "\n" +
                                 "Dexterity: " + userData.getStat("dexterity") + "\n" +
                                 "Agility: " + userData.getStat("agility") + "\n" +
                                 "Endurance: " + userData.getStat("endurance") + "\n" +
                                 "Willpower: " + userData.getStat("willpower") + "\n" +
                                 "Magic: " + userData.getStat("magic") + "\n" +
                                 "\n";
 	int xCenter = 700 / 2;
 	
g2d.setFont(new Font("Lucida Handwriting", Font.BOLD, 30)); 
   // Calculate the x position for center alignment
   FontMetrics fm = g2d.getFontMetrics();
  
 	
	  String[] Sections = text.split("\n" + "\n"); 
	  int textWidth = fm.stringWidth(Sections[0]);
   int xPosition = xCenter - (textWidth / 2);
     // Draw each line of text
     int x = xPosition;  // X-coordinate of the text
     int y = 95;  // Starting Y-coordinate of the text
     int lineHeight;
     g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
     g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
     g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
     for (String section : Sections) {
     String[] lines = section.split("\n");
     for (String line : lines) {
   	  lineHeight = g2d.getFontMetrics().getHeight();
         g2d.drawString(line, x, y);
         y += lineHeight;
     }
     if (section.equals(Sections[2])) {
   	  // levels
   	y = 225; 
   	x = 80;
   	g2d.setFont(new Font("Lucida Handwriting", Font.BOLD, 15)); 
     }
     if (section.equals(Sections[3])) {
   	  // stats
   	  x=250;  
     	y = 225;
     	g2d.setFont(new Font("Lucida Handwriting", Font.BOLD, 15)); 
       }   if (section.equals(Sections[0])) {
   	  //race
     	x=90;  
     	y = 140;
     	g2d.setFont(new Font("Lucida Handwriting", Font.BOLD, 18)); 
       }
     if (section.equals(Sections[1])) {
   	  //alignment
     	x=400;  
     	y = 140;
     	g2d.setFont(new Font("Lucida Handwriting", Font.BOLD, 18)); 
       }
     }
	     
	    //opacity = 1.0f;
	    //g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,opacity));
     g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
     g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
     g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
     InputStream inputStream1;
 	if (!userData.Avatar.equals("")) {
 		inputStream1 = downloadImage(userData.Avatar);
 	
     ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
     byte[] buffer1 = new byte[4096];
     int bytesRead1;
     while ((bytesRead1 = inputStream1.read(buffer1)) != -1) {
         outputStream1.write(buffer1, 0, bytesRead1);
     }

     // Convert image data to byte array
     byte[] imageData1 = outputStream1.toByteArray();
     ByteArrayInputStream bis1 = new ByteArrayInputStream(imageData1);
     BufferedImage additionalImage1 = ImageIO.read(bis1);
    System.out.println();

	   g2d.drawImage(additionalImage, additionalImage.getWidth(), additionalImage.getHeight(), null);
	   g2d.drawImage(additionalImage1, 497, 200, null);}
 	
	    g2d.dispose();
	    try {
	        ImageIO.write(additionalImage, "png", baos);
	    } catch (IOException e) {
	        e.printStackTrace();
	        event.getChannel().sendMessage("Failed to create image.").queue();
	        return;
	    }

	    imageData = baos.toByteArray();
	    ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
	    try {
	        event.getChannel().sendMessage("").addFiles(FileUpload.fromData(bais, "image.png")).queue();
	    } catch (Exception e) {
	        e.printStackTrace();
	        event.getChannel().sendMessage("Failed to send image.").queue();
	        
	    }} catch (IOException e) {
			e.printStackTrace();
		}
		
	    }
public static Color getMostFrequentBorderColor(BufferedImage image) {
    Map<Color, Integer> colorCountMap = new HashMap<>();

    int width = image.getWidth();
    int height = image.getHeight();

    // Iterate over the border pixels and count the colors
    for (int x = 0; x < width; x++) {
        incrementColorCount(colorCountMap, new Color(image.getRGB(x, 0)));
        incrementColorCount(colorCountMap, new Color(image.getRGB(x, height - 1)));
    }
    for (int y = 0; y < height; y++) {
        incrementColorCount(colorCountMap, new Color(image.getRGB(0, y)));
        incrementColorCount(colorCountMap, new Color(image.getRGB(width - 1, y)));
    }

    // Find the most frequent color
    Color mostFrequentColor = null;
    int maxCount = 0;
    for (Map.Entry<Color, Integer> entry : colorCountMap.entrySet()) {
        if (entry.getValue() > maxCount) {
            mostFrequentColor = entry.getKey();
            maxCount = entry.getValue();
        }
    }

    return mostFrequentColor;
}
private static void incrementColorCount(Map<Color, Integer> colorCountMap, Color color) {
    colorCountMap.put(color, colorCountMap.getOrDefault(color, 0) + 1);
}
public static Color decreaseBrightness(Color color, double factor) {
    // Ensure the factor is within a reasonable range
    if (factor < 0.0) {
        factor = 0.0;
    } else if (factor > 2.0) {
        factor = 1.0;
    }

    // Scale down the RGB components
    int red = (int) (color.getRed() * factor);
    int green = (int) (color.getGreen() * factor);
    int blue = (int) (color.getBlue() * factor);

    // Ensure the values are within valid range [0, 255]
    red = Math.min(255, Math.max(0, red));
    green = Math.min(255, Math.max(0, green));
    blue = Math.min(255, Math.max(0, blue));

    return new Color(red, green, blue);
}
public void returnURL(MessageReceivedEvent event) {
String url = "";
Member member = event.getMember();
String userId = member.getId();
UserData userData = userDataMap.get(userId);
if (userData.Proxy) {
    userId = userData.ProxyUserID;
}
userData = userDataMap.get(userId);

if (event.getAuthor().isBot()) return;
List<Message.Attachment> attachments = event.getMessage().getAttachments();

if (attachments.isEmpty()) return; // No attachments
Message.Attachment attachment = attachments.get(0); // Assuming there's only one attachment
if (!attachment.isImage()) return; // Ensure it's an image attachment
String newURL="";
try {
    url = new URL(attachment.getUrl()).toString();
   newURL = getURL2(event, url);
} catch (MalformedURLException e) {
	event.getChannel().sendMessage("Image is incompatible, please try again").queue();
}

System.out.println(newURL);
}
public String getURL2(MessageReceivedEvent Event, String URL) {
	String DataUrl = "";
	 Member member = Event.getMember();
	    String userId = member.getId();
	    UserData userData = userDataMap.get(userId);
	    if (userData.Proxy) {
	    	userId = userData.ProxyUserID;
	    }
	    userData = userDataMap.get(userId);

	  try {
	
	 URL url = new URL(URL);
  HttpURLConnection connection = (HttpURLConnection) url.openConnection();
  connection.setRequestMethod("GET");

  // Read image data from connection
  InputStream inputStream = connection.getInputStream();
  ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  byte[] buffer = new byte[4096];
  int bytesRead;
  while ((bytesRead = inputStream.read(buffer)) != -1) {
      outputStream.write(buffer, 0, bytesRead);
  }

  // Convert image data to byte array
  byte[] imageData = outputStream.toByteArray();

  String base64Image = Base64.getEncoder().encodeToString(imageData);

  // Construct the Data URL
  DataUrl = "data:image/png;base64," + base64Image;
  
  // Obtain URL of the saved file

  outputStream.close();
 
		inputStream.close();
	
  connection.disconnect();
  

//catch () {
}catch (IOException e) {
		e.printStackTrace();
	}
return DataUrl;
}
    public static List<String> splitString(String input, int maxLength) {
        List<String> result = new ArrayList<>();
        int length = input.length();
        int start = 0;

        while (start < length) {
            int end = Math.min(length, start + maxLength);
            
            // Try to break at a space or a newline character
            if (end < length) {
                int lastSpace = input.lastIndexOf(' ', end);
                int lastNewline = input.lastIndexOf('\n', end);

                if (lastNewline > start) {
                    end = lastNewline + 1;
                } else if (lastSpace > start) {
                    end = lastSpace + 1;
                }
            }

            result.add(input.substring(start, end).trim());
            start = end;
        }

        return result;
    }
}