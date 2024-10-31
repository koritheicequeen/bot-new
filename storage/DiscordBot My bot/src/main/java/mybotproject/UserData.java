package mybotproject;

import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class UserData {
	
    private String userId;
    HashMap<String, Integer> stats;
    Map<String, String> Cdata;
    LinkedHashMap<String, String> Abilities;
    LinkedHashMap<String, String> Armor;
    LinkedHashMap<String, String> Weapon;
    LinkedHashMap<String, String> Enchant;
    LinkedHashMap<String, String> Bag;
    LinkedHashMap<String, Integer> Currency;
    List<String> availableEvolutions;
    int proxyInt = 1;
    int levelUpPoints;
    static LinkedHashMap<String, String> Humanoid;
    static LinkedHashMap<String, String> DemiHuman;
    static LinkedHashMap<String, String> Monster;
    public String Avatar;
    public static List<String> ProxyUserIds = new ArrayList<>();
   
    String ProxyUserID;
    public UserData(String userId) {
    	
        this.userId = userId;
        this.stats = new HashMap<>(); 
        this.stats.put("HP", 0);
        this.stats.put(" LeftoverEXP", 0);
        this.stats.put("vitality", 0);
        this.stats.put("strength", 0);
        this.stats.put("dexterity", 0);
        this.stats.put("agility", 0);
        this.stats.put("endurance", 0);
        this.stats.put("willpower", 0);
        this.stats.put("magic", 0);
        this.stats.put("EXP", 0);
        this.stats.put("Health", 0);
      
        
      
        this.Cdata = new HashMap<>();
        this.Cdata.put("Race", "");
        this.Cdata.put("Note", "");
        this.Cdata.put("Character Name", "");
        this.Cdata.put("Alignment", "");
        
        this.Abilities = new LinkedHashMap<>();
        this.Armor = new LinkedHashMap<>();
        this.Bag = new LinkedHashMap<>();
        this.Weapon = new LinkedHashMap<>();
        this.Enchant = new LinkedHashMap<>();
        this.Currency = new LinkedHashMap<>();
        this.Currency.put("bronze", 0);
        this.Currency.put("silver", 0);
        this.Currency.put("gold", 0);
        this.Currency.put("stellar-gold", 0);
        this.availableEvolutions = new ArrayList<>();
        
        UserData.Humanoid = new LinkedHashMap<>();
        UserData.DemiHuman = new LinkedHashMap<>();
        UserData.Monster = new LinkedHashMap<>();
        UserData.Humanoid.put("Human",
        		"```"
        		+ "Description: A versatile and resilient race known for their adaptability and determination. Humans can thrive in a variety of environments and possess a unique ability to learn and master different skills quickly"+"\n"
        		+ "Versatile Growth: Start with 30 points instead of 20.\r\n"
        		+ "Adaptive Learner: Gain 1.25x more EXP for leveling up."+ "```" +"\n"
        		+ "Help: Level"
        		);
        
        UserData.Humanoid.put("Enlightened Human", 
        		"```"
        		+ "Description: Humans who have attained higher knowledge and spiritual awareness. They often seek wisdom and understanding, transcending mundane concerns to achieve a greater sense of purpose."+"\n"
        		+ "Much to Learn Gain 1.2x more EXP for leveling up.\r\n"
        		+ "Insightful: Once per day, ask a single question about the game world and receive a truthful answer."+ "```"+"\n"
        		+ "Help: Level"
        		);
        
        UserData.Humanoid.put("Divine Human", "```"
        		+ "Description: Humans blessed with divine power and celestial attributes. These individuals are often chosen by the gods or possess a sacred lineage, granting them extraordinary abilities and a noble presence."+"\n"
        		+ "Divine Fury: Once per combat, deal 100% additional damage with all attacks for a turn.\r\n"
        		+ "Aura of Chaos: Enemies have Disadvantage on their attack rolls against you until you are damaged."+ "```"+"\n"
        		+ "Help: Alignment"
        		);
        
        UserData.Humanoid.put("Human (OtherWorlder)", "```"
        		+ "Description: Humans who have traveled from another world, often possessing unique abilities. Their experiences in different realms grant them knowledge and skills uncommon in their new environment."+"\n"
        		+ "Dimensional Knowledge: Gain 2 more ability slots.\r\n"
        		+ "Otherworldly Adaptation: 20% less EXP but start at level 5 with 45 stats instead of 20."+ "```"+"\n"
        		+ "Help:Level"
        		);
        UserData.Humanoid.put("Enlightened Human (OtherWorlder)", "```"
        		+ "Description: Humans who have traveled from another world, often possessing unique abilities. Their experiences in different realms grant them knowledge and skills uncommon in their new environment."+"\n"
        		+ "Cosmic Insight: Once per combat, reduce the stats of a single monster or demihuman by 50%\r\n"
        		+ "Planar Resistance: Reduce damage from magical attacks by 10%"+ "```"+"\n"
        		+ "Help:Level"
        		);
        
        UserData.Humanoid.put("Divine Human (OtherWorlder)",
        		
        		"```"
        		+ "Description: OtherWorlders endowed with divine powers, making them exceptional beings. They carry the blessings and mandates of higher powers, often becoming pivotal figures in their new world"+"\n"+
        		"Divine Protection:Once per combat, reduce the stats of all monsters and demi Humans of less level by half for 2 turns.\r\n"
        		+ "Radiant Presence: Allies gain Advantage on attack rolls."+ "```"+"\n"
        		+ "Help:Alignment"
        		);
        
        UserData.Humanoid.put("Halfling",
        		"```"
        				+ "Description: Small, agile beings known for their resourcefulness and quick wit. They are often underestimated due to their size but possess a keen intellect and remarkable dexterity, making them excellent adventurers."+"\n"+ 
        		"Luck of the Small: Once per combat, reroll one roll.\r\n"
        		+ "Stealthy Steps: Gain Advantage on Stealth checks."	+ "```"+"\n"
        		+ "Help:Level"
        	);
        
        UserData.Humanoid.put("Archaic Halfling", 
        		"```"
        				+ "Description: Halflings with a deep connection to ancient traditions and forgotten lore. Their understanding of old ways and hidden knowledge gives them an edge in navigating both the physical and mystical realms."+"\n" +
        		"Hidden Potential: User mimic one skill from anyone they fight if they have the slot, however, they lose it after 24 hours\r\n"
        		+ "Mystic Connection: Invisibility magic that conceals them entirely from all but the most perceptive."	+ "```"+"\n"
        		+ "Help: Doesnt Evolve"
        	);
        
        UserData.Humanoid.put("Dwarf", 
        		"```"
        	+ "Description: Stout and sturdy beings known for their craftsmanship and mining skills. They have a strong sense of community and tradition, often living in underground cities where they forge powerful weapons and beautiful artifacts."+"\n"+
        		"Stout Constitution: Reduce incoming damage from poisons 50%\r\n"
        		+ "Resilient Spirit: Immune to fear effects."+ "```"+"\n"
        		+ "Help:Level"
        		);
        
        UserData.Humanoid.put("High Dwarf",
        		"```"
        		+ "Description: Dwarves of noble lineage, renowned for their exceptional skills and leadership. They hold positions of power and influence, guiding their people with wisdom and strength, and often possessing superior abilities in smithing and combat." +"\n"+
        		"Stone Mastery: Armor has double bonus when wielded or crafted by them.\r\n"
        		+ "Mountain’s Strength: Once per combat, reduce all damage."	+ "```"+"\n"
        		+ "Help: Doesnt Evolve"
        	);
        
        UserData.Humanoid.put("Elf", 
        		"```"
        		+ "Description: Graceful and long-lived beings with a deep affinity for nature and magic. They are known for their beauty, wisdom, and elegance, often living in harmony with their surroundings and excelling in archery and spellcasting." +"\n"+
        		"Elven Agility: Agility reduces damage taken by half of the roll if fail to fully dodge.\r\n"
        		+ "Nature's Insight: Can tell what race someone is by sight."+ "```"+"\n"
        		+ "Help: Level and Affinity"
        		);
        
        UserData.Humanoid.put("High Elf", 
        		"```" 
        	+ "Description: Elves of noble heritage, possessing advanced magical abilities and wisdom. They are often the leaders and scholars of elven society, revered for their knowledge and mastery of the arcane arts."+"\n" +
        		"Homunculi: Creates a body double that acts independently\r\n"
        		+ "Arcane Knowledge: Agility reduces damage taken by 100% of the roll on fail"	+ "```"+"\n"
        		+ "Help: Doesnt Evolve"
        	);
        
        UserData.Humanoid.put("Dark Elf", 
        		"```"
        				+ "Description: Elves who dwell in shadows and practice dark magic, often shunned by other elves. They have a reputation for being cunning and ruthless, using their abilities for personal gain or vengeance."+"\n" +
        		"Shadow Affinity: Gain +10 to Magic rolls when casting dark spells.\r\n"
        		+ "True Vision: Can see someone's entire stat sheet by sight and ignores all forms of stealth."+ "```"+"\n"
        		+ "Help: Doesnt Evolve"
        		);
        
        UserData.Humanoid.put("Rabbit Folk", 
        		"```"
        				+ "Description: Small, agile humanoids with the features of rabbits. Rabbitfolk are known for their speed, cunning, and ability to escape from danger. They live in tight-knit communities and are adept at using their environment to their advantage, often creating intricate burrow systems to protect themselves and their kin."+"\n" +
        		"Quick Reflexes: Gain Advantage on dodging rolls.\r\n"
        		+ "Nimble Feet: Once per day, auto escape an encounter"+ "```"+"\n"
        		+ "Help: Unknown"
        		);
        
        UserData.Humanoid.put("Spirit Rabbit",
        		
        		"```"
        		+ "Description: Ethereal beings that embody the essence of the rabbit, often associated with luck and agility. Spirit Rabbits can traverse the spirit world and the physical realm, using their mystical abilities to guide and protect those they deem worthy. They are revered in many cultures for their connection to the spiritual and natural worlds." +"\n"
        		+ "Mystic Evolution: They can use Agility to attack with melee.\r\n"
        		+ "Nature's Abundance: Allies, including them, gain 1.5x EXP from combat."	+ "```"+"\n"
        		+ "Help: Doesnt Evolve"
        		);
        
        UserData.Humanoid.put("Witch",
        		"```"
        				+ "Description:  Practitioners of magic with a deep connection to mystical forces. They harness the natural and supernatural energies around them, using their knowledge to cast spells and brew potions." 
        		+ "Magical Affinity: Gain Advantage on Magic-based resistances checks.\r\n"
        		+ "Arcane Knowledge: All resistances have 1dmagic/2 added to them"+ "```"+"\n"
        		+ "Help: Alignment and Level"
        		);
        
        UserData.Humanoid.put("Evil Witch",
        		"```"
        				+ "Description: Witches who have embraced dark magic, using it for malicious purposes. They often engage in nefarious activities, driven by a desire for power or revenge, and are feared by many." +"\n"
        		+ "Dark Magic Mastery: Deal 25% additional damage with Dark Magic or 50% to anyone with light magic\r\n"
        		+ "Malevolent Aura: Enemies take magic/25 damage per turn (ignoring resistances)."+ "```"+"\n"
        		+ "Help: Level"
        		);
        
        UserData.Humanoid.put("Dark Divinity Witch",
        		"```"
        				+ "Description: Witches who channel the powers of dark gods, wielding formidable and sinister magic. Their pacts with dark deities grant them immense power but often come at a steep personal cost." +"\n"
        		+ "Necrotic Touch: Once per day, cause one target to take double damage from all sources until combat ends.\r\n"
        		+ "Devouring Abyss: Absorb magic/10 hp from everyone per turn of combat, ignoring all resistances (optional)"+ "```"+"\n"
        		+ "Help: Doesnt Evolve "
        		);
        
        UserData.Humanoid.put("Benevolent Witch", 
        		"```"
        		+ "Description: Witches who use their magical abilities for good, often aiding and healing others. They are respected and revered in their communities for their kindness and altruistic actions." +"\n"
        		+ "Healing Light: Once per combat, heal an ally for 100% of their max HP, half as effective on yourself.\r\n"
        		+ "Protective Aura: Allies gain Advantage on defense rolls on turns the user doesn't attack."	+ "```"+"\n"
        		+ "Help: Level"
        	);
        
        UserData.Humanoid.put("Light Divinity Witch", 
        		"```"
        		+ "Description: Witches who draw power from benevolent deities, specializing in holy and protective magic. Their magic is often used to ward off evil, heal the sick, and bring blessings to those in need." +"\n"
        		+ "Radiant Burst: Once per combat, stuns all enemies for 1 turn and heals all allies, including yourself 100%\r\n"
        		+ "Holy Protection: Gain immunity to Dark Magic for 2 turns, once per combat, otherwise 50% resistance." 	+ "```"+"\n"+
        		"Help: Doesnt Evolve"
        	);
        
        UserData.Humanoid.put("Sorceress", 
        		"```"
        		+ "Description: Female spellcasters with innate magical abilities, often specializing in a particular school of magic. Their natural talent for magic sets them apart, allowing them to perform feats that others can only achieve through rigorous study." +"\n"
        		+ "Elemental Affinity: Gains 20% bonus damage when using wind, water, fire, or earth magic.\r\n"
        		+ "Arcane Reservoir: Once per day, reset all cooldowns"	+ "```"+"\n"
        		+ "Help:Level"
        	);
        
        UserData.Humanoid.put("Primordial Sorceress", 
        		"```"
        		+ "Description: Sorceresses who command ancient and elemental forces, possessing vast and primal power. Their connection to the fundamental aspects of the world grants them control over earth, fire, water, and air." +"\n"
        		+ "Primordial Power: Once per day, reset the cooldown of all allies.\r\n"
        		+ "Elemental Mastery: Gain immunity to one elemental damage"+ "```"+"\n"
        		+ "Help:Doesnt Evolve"
        		);
        
        UserData.DemiHuman.put("Merfolk",
        		"```"
        				+ "Description: Aquatic beings with the upper body of a human and the lower body of a fish, known for their beauty and singing. They inhabit oceans and rivers, using their enchanting voices to communicate and sometimes to lure sailors." +"\n"
        		+ "Aquatic Adaptation: rolls are increased by 50% when submerged in water.\r\n"
        		+ "Water Breathing: 50% resistance to water damage"+ "```"+"\n"
        		+ "Help:Level"
        		);
        
        UserData.DemiHuman.put("Siren", 
        		"```"
        		+ "Description: Enchanting merfolk with a mesmerizing voice, capable of luring sailors to their doom. Their songs are both beautiful and dangerous, leading those who hear them into perilous situations." +"\n"
        		+ "Enchanting Song: Once per combat, charm one enemy for 1 turn (must succeed on a Willpower saving throw).\r\n"
        		+ "Aquatic Grace: Flood a channel, Sirens gain 50% bonus rolls and all water magic is increased by 50% for 3 turns. Water magic ignores all immunities while flooded."	+ "```"+"\n"
        		+ "Help:Level"
        	);
        
        UserData.DemiHuman.put("Lycanthrope", 
        		"```"
        				+ "Description: Beings capable of transforming into powerful, beastly forms. Their dual nature grants them enhanced strength, speed, and senses, but they often struggle with controlling their animalistic instincts." +"\n"
        		+ "Beast Form: Transform into a beast or into a human adding 1dStrength/4 to every roll once per day\r\n"
        		+ "Enhanced Senses: Gain Advantage against stealth"+ "```"+"\n"
        		+ "Help: Level"
        		);
        
        UserData.DemiHuman.put("Spiritual Beast", 
        		"```"
        		+ "Description: Lycanthropes with a strong connection to the spirit world, possessing unique spiritual abilities. They can communicate with spirits, draw upon spiritual energy, and use their powers for guidance, protection, or combat." +"\n"
        		+ "Spiritual Bond: The user can freely switch back and forth between beast and human adding 1dStrength/2 when in beast form, and 1dMagic/2 when in Human\r\n"
        		+ "Spectral Claws: Once per combat, when in your beast form, deal 1dmagic/4 unreducible" + "```"+"\n"
        		+ "Help: Level and Alignment"
        		);
        
        UserData.DemiHuman.put("Divine Beast",
        		"```"
        				+ "Description: A creature of immense power, blessed by the gods with divine strength and wisdom. Divine Beasts are revered by many and feared by those who oppose them. They often possess abilities that transcend the natural world, embodying the raw power and will of the divine entities that blessed them." +"\n"
        		+ "Divine Strength: Once per combat, deal maximum damage with an attack.\r\n"
        		+ "Holy Presence: Allies have 25% lifedrain" + "```"+"\n"
        		+ "Help: Doesnt Evolve"
        		);
        
        UserData.DemiHuman.put("Kobold", 
        		"```"
        				+ "Description: Small, dog-like humanoids known for their agility and loyalty, often serving stronger monsters." +"\n"
        		+ "Greedy Kobolds: Prices are 25% cheaper from NPC's.\r\n"
        		+ "Cunning Tactics: Once per combat, distract an enemy, causing them to take Disadvantage for a turn."+ "```"+"\n"
        		+ "Help: Unknown"
        		);
        
        UserData.DemiHuman.put("True Chimera",
        		"```"
        				+ "Description: A fearsome beast composed of multiple animal parts, the True Chimera is a creature of legend. It possesses the strength, speed, and senses of its various components, making it a formidable adversary. These creatures are often the result of dark magic or ancient experiments gone awry." +"\n"
        		+ "Hidden Potential: Their attacks either inflict bleed (Magic/4), poison (Magic/2) or disadvantage for 2 turn\r\n"
        		+ "Forsaken by the Gods: Immunity to all statuses."+ "```"+"\n"
        		+ "Help:Doesnt evolve"
        		);
        
        UserData.DemiHuman.put("Orc", 
        		"```"
        				+ "Description: A race of robust, humanoid creatures with a strong physique resembling boars, typically known for their brute strength and resilience." +"\n"
        		+ "Brutal Strength: Gain a +4 to Strength rolls when in combat.\r\n"
        		+ "Battle Cry: Once per combat, inspire allies, giving them Advantage on their next attack roll."+ "```"+"\n"
        		+ "Help: Level"
        		);
        
        UserData.DemiHuman.put("Orc Knight", 
        		"```"
        		+ "Description:  Elite orcs who lead their kin in battle, distinguished by their superior strength and tactical prowess." +"\n"
        		+ "Defender’s Resolve: Can roll raw endurance when defending an ally\r\n"
        		+ "Shield Bash: Once per combat, knock back an enemy, causing them to lose their next action."+ "```"+"\n"
        		+ "Help: Level"
        		);
        
        UserData.DemiHuman.put("Orc General", 
        		"```"
        				+ "Description:  Elite orcs who lead their kin in battle, distinguished by their superior strength and tactical prowess." +"\n"
        		+ "Tactical Mastery: Once per combat, give a command that grants an ally 100% bonus strength (1dstrength*2).\r\n"
        		+ "Warrior’s Endurance: Reduce incoming damage by 50% points for 1 turn."+ "```"+"\n"
        		+ "Help: Level/alignment"
        		);
        
        UserData.DemiHuman.put("Orc Lord", 
        		"```"
        				+ "Description: A rare and powerful evolution of orcs, capable of commanding vast armies and wielding immense strength." +"\n"
        		+ "Starved Aura: All allies are immune to stuns but cant stop fighting until their enemies are dead once combat starts\r\n"
        		+ "Drain Life: Each turn in combat, drain Magic/10 health from all enemies and restore your own HP by the same amount."+ "```"+"\n"
        		+ "Help: Doesnt Evolve"
        		);
        
        UserData.DemiHuman.put("High Orc",
        		"```"
        				+ "Description: An advanced form of orcs with enhanced intelligence and combat abilities, often serving in leadership roles." +"\n"
        		+ "Enhanced Healing: Healing received is increased by 20%, including passive regen.\r\n"
        		+ "Fortified Will: 50% chance to resist statuses."+ "```"+"\n"
        		+ "Help: Level"
        		);
        
        UserData.DemiHuman.put("Spirit Boar",
        		"```"
        				+ "Description: A mystical creature with a boar-like appearance, embodying elemental or spiritual energies." +"\n"
        		+ "Spiritual Charge: Once per combat, perform a charge attack that deals additional damage equal to your Strength stat.\r\n"
        		+ "Nature’s Blessing: Allies gain +10 armor value"	+ "```"+"\n"
        		+ "Help: Level"
        	);
        
        UserData.DemiHuman.put("Divine Boar", 
        		"```"
        				+ "Description: A powerful, sacred beast that combines the traits of a boar with divine attributes, often possessing immense strength and magical abilities." +"\n"
        		+ "Holy Rampage: Once per combat, deal maximum damage with a charge attack that cant be reduced.\r\n"
        		+ "Blessed Resilience: Immune to statuses."+ "```"+"\n"
        		+ "Help: Doesnt Evolve"
        		);
        
        UserData.DemiHuman.put("LizardMen",
        		"```"
        				+ "Description: Reptilian humanoids known for their strength and adaptability. Lizardmen live in tribal societies, often in swamps or jungles, and are skilled hunters and warriors. They possess a natural affinity for water and can be formidable opponents in combat." +"\n"
        		+ "Scaly Hide: +2 armor value\r\n"
        		+ "Amphibious: Immunity to disadvantage from non mental sources."+ "```"+"\n"
        		+ "Help:Level"
        		);
        
        UserData.DemiHuman.put("Dragonnewt", 
        		"```"
        				+ "Description: Ancient, dragon-like beings with immense power and wisdom. Dragonnewts are revered for their connection to the dragon lineage, possessing abilities that reflect their draconic heritage. They can harness elemental powers and are often seen as wise leaders and protectors of their kin." +"\n"
        		+ "Dragon’s Might: +5 armor value\r\n"
        		+ "Elemental Breath: Once per combat, use a breath attack that deals damage 1dmagic aoe."+ "```"+"\n"
        		+ "Help:Level"
        		);
        
        UserData.DemiHuman.put("True Dragonnewt", 
        		"```"
        				+ "Description: The purest form of Dragonnewts, possessing unparalleled strength and magical prowess. True Dragonnewts are rare and revered, often seen as the embodiment of draconic power and wisdom. They wield immense power, capable of influencing the elements and commanding respect from all " +"\n"
        		+ "Draconic Power: once per combat, use a breath attack that deals 2dmagic aoe.\r\n"
        		+ "Elemental Mastery: Twice per combat, resist an elemental attack entirely."+ "```"+"\n"
        		+ "Help: Doesnt Evolve"
        		);
        
        UserData.DemiHuman.put("Gozu",
        		"```"
        				+ "Description: A fearsome humanoid with the head of a bull, Gozu are powerful warriors often associated with the underworld. Their brute strength and imposing presence make them formidable adversaries in battle." +"\n"
        		+ "Bull’s Strength: Gain advantage to Strength when charging.\r\n"
        		+ "Ferocious Charge: Once per combat, perform a charge attack that deals an additional 1dStrength/2."	+ "```"+"\n"
        		+ "Help:Level"
        	);
        
        UserData.DemiHuman.put("Gyuuki", 
        		"```"
        				+ "Description: Half-human, half-ox beings with immense strength and endurance. Gyuuki are known for their resilience and are often found guarding sacred places or serving as protectors of ancient relics."+"\n" 
        		+ "Mystic Horns: Once per combat, use your horns to deal additional damage equal to your Strength stat.\r\n"
        		+ "True Strength Once per combat, damage cant be reduced."+ "```"+"\n"
        		+ "Help: Doesnt Evolve"
        		);
        
        UserData.DemiHuman.put("Mezu",
        		"```"
        				+ "Description: A fearsome beast with the body of a horse and the head of a bull. Mezu are known for their incredible strength and tenacity, often serving as enforcers in the underworld." +"\n"
        		+ "Brute Strength: Magic is reduced by half but Strength is increased by 50%\r\n"
        		+ "Horse Resilience: KO doesnt occur until -MaxHP/2"+ "```"+"\n"
        		+ "Help: Level"
        		);
        
        UserData.DemiHuman.put("Baki", 
        		"```"
        				+ "Description: Horse-like oni, known for their incredible speed and strength, often serving as swift and powerful warriors." +"\n"
        		+ "Equal Field: Once per combat, use the magic stat of your opponent.\r\n"
        		+ "Physical Force: Strength is increased by 100% but magic is decreased by 100%"+ "```"+"\n"
        		+ "Help: Doesnt Evolve"
        		);
        
        UserData.DemiHuman.put("Deathman", 
        		"```"
        				+ "Description: Undead beings that retain a semblance of their former selves. Deathmen are driven by an insatiable hunger for life force and are often used as shock troops in necromancer armies." +"\n"
        		+ "Soul Collection:: Collecting an Dead OC's soul allows you to consume it and upon release, increase your magic stat by 25% per soul released.\r\n"
        		+ "Grim Determination: Once per combat, resist a stun effect."+ "```"+"\n"
        		+ "Help:Doesnt Evolve"
        		);
        
        UserData.Monster.put("Goblin",
        		"```"
        				+ "Description:  Small, mischievous creatures known for their cunning and resourcefulness. They often live in tribes, scavenging and creating clever traps, and are skilled at surviving in harsh environments." +"\n"
        		+ "Nimble Fingers: Dexterity can be used for melee attacks.\r\n"
        		+ "Quick Reflexes: Once per combat, gain Advantage on the next attack roll."+ "```"+"\n"
        		+ "Help: Level based on gender"
        		);
        
        UserData.Monster.put("Hobgoblin", 
        		"```"
        				+ "Description: Larger and more disciplined goblins, often serving as warriors or leaders. They are stronger and more organized than their smaller kin, excelling in combat and strategy." +"\n"
        		+ "Warrior’s Strength: Gain a +4 to Strength rolls when in combat.\r\n"
        		+ "Battle Tactics: Gain Advantage on the first attack roll in combat or when in combat with an ally."	+ "```"+"\n"
        		+ "Help:Level"
        	);
        
        UserData.Monster.put("Goblina", 
        		"```"
        				+ "Description: Female goblins, known for their cunning and agility. They play vital roles in goblin society, often outsmarting their enemies and leading their tribes with sharp intellect and quick reflexes." +"\n"
        		+ "Cunning Mind: Boosts magic by willpower/2 \r\n"
        		+ "Deceptive Appearance: Once per combat, distract an enemy, causing them to take Disadvantage on their next roll."+ "```"+"\n"
        		+ "Help:Level"
        		);
        
        UserData.Monster.put("Ogre", 
        		"```"
        				+ "Description: Large, brutish beings known for their strength and ferocity. They are formidable opponents in battle, relying on their physical power to overwhelm enemies, but often lack subtlety and intelligence." +"\n"
        		+ "Specialty: Once per combat, make an attack dealing double damage.\r\n"
        		+ "Tough Hide: Immune to weapon bonuses"+ "```"+"\n"
        		+ "Help:Level"
        		);
        
        UserData.Monster.put("Kjinn", 
        		"```"
        				+ "Description: Mystical ogres with the ability to manipulate natural elements. Their control over earth, fire, water, and air makes them versatile and dangerous, blending brute force with elemental magic." +"\n"
        		+ "Mystic Essence: Once per combat, turn a skill into an Aoe\r\n"
        		+ "Elemental Affinity: Choose one element (Fire, Water, Earth, Air) to gain Immunity against for 1 turn."+ "```"+"\n"
        		+ "Help: Level, Level and affinity"
        		);
        
        UserData.Monster.put("Oni", 
        		"```"
        				+ "Description: Demonic ogres from Japanese folklore, possessing immense strength and dark powers. They are feared for their destructive capabilities and malevolent nature, often serving as enforcers of dark forces." +"\n"
        		+ "Demonic Strength: Gain a +5 to Strength or +5 to Dexterity rolls when in combat.\r\n"
        		+ "Fearsome Presence: All enemies have disadvantage if they are a lower Level."+ "```"+"\n"
        		+ "Help: Level + Unknown"
        		);
        
        UserData.Monster.put("Divine Oni",
        		"```"
        				+ "Description: Oni blessed with divine powers, combining their natural strength with holy attributes. They are rare and revered, using their abilities to protect or conquer, depending on their alignment." +"\n"
        		+ "Irresistible Force: Once per combat, deal maximum damage with an attack.\r\n"
        		+ "Divine Resilience: Immune to stunning effects."+ "```"+"\n"
        		+ "Help: Level"
        		);
        UserData.Monster.put("Divine Fighter Oni", 
        		"```"
        				+ "Description: Oni who excel in combat, blessed with divine strength and skill. They are unparalleled warriors, capable of facing multiple opponents and emerging victorious through sheer might and divine favor." +"\n"
        		+ "Heavenly Strike: Attack damage cant be reduced and effects that ignore death are nullified\r\n"
        		+ "Blessed Aura: Allies gain Advantage on their first attack roll."+ "```"+"\n"
        		+ "Help: Doesnt Evolve"
        		);
        
        UserData.Monster.put("Spirit Oni", 
        		"```"
        				+ "Description: Oni with a strong connection to the spiritual realm, capable of manipulating spirits. Their abilities allow them to interact with ghosts and other ethereal beings, using this connection for various purposes." +"\n"
        		+ "Spiritual Manifestation: The user cannot die\r\n"
        		+ "Ethereal Sight: The user has advantage on all rolls when fighting someone who has magic as their highest stat"	+ "```"+"\n"
        		+ "Help: Level"
        	);
        UserData.Monster.put("Death Oni", 
        		"```"
        				+ "Description: Oni who command death and decay, wielding necromantic powers. They can summon the dead, cause rapid aging, and spread disease, making them formidable and feared necromancers." +"\n"
        		+ "Necrotic Touch: Once per combat, deal 2dMagic unreducable to all enemies, and heal yourself equal to damage dealt\r\n"
        		+ "Aura of Despair: Enemies are stunned until your next turn after every successful hit."+ "```"+"\n"
        		+ "Help: Doesnt Evolve"
        		);
        
        UserData.Monster.put("Mystic Oni", 
        		"```"
        				+ "Description: Oni with profound magical abilities, specializing in mystical arts. They are adept at casting spells and performing rituals, often possessing knowledge of ancient and powerful magic." +"\n"
        		+ "Mystic Power: Gain a +10 to Magic rolls when casting spells.\r\n"
        		+ "Mystic Shield: Once per combat, create a shield that absorbs damage equal to your Magic stat."+ "```"+"\n"
        		+ "Help: Level"
        		);
        
        UserData.Monster.put("Wicked Oni", 
        		"```"
        				+ "Description: Malicious oni who revel in chaos and destruction. Their evil nature drives them to sow discord and wreak havoc, delighting in the suffering they cause." +"\n"
        		+ "infernal Flame: Every time you take damage, deal 1dmagic/4 unreducible to an enemy.\r\n"
        		+ "Fiery Aura: All resistances are turned to weaknesses and immunities are 100% more damage instead"+ "```"+"\n"
        		+ "Help: Doesnt Evolve"
        		);
        
        UserData.Monster.put("Enki", 
        		"```"
        				+ "Description: Oni who have transcended their demonic nature, attaining a higher spiritual state. They are enlightened beings, balancing their formidable strength with wisdom and inner peace." +"\n"
        		+ "Ancient Knowledge: They can learn every fire skill used against them\r\n"
        		+ "Mystic Presence: Immunity to fire and 100% bonus damage when using fire"	+ "```"+"\n"
        		+ "Help: Doesnt evolve"
        	);
        
        UserData.Monster.put("Tengu", 
        		"```"
        				+ "Description: Mythical bird-like creatures known for their martial prowess and wisdom. Tengus are often depicted with both human and avian features, and they are masters of the sword and stealth. They are protectors of sacred places and are respected for their knowledge of the ancient arts." +"\n"
        		+ "Flight: Advantage against Strength based attacks\r\n"
        		+ "Allies of the Wolves The user has advantage when in combat with allies."	+ "```"+"\n"
        		+ "Help: Level?"
        	);
        
        UserData.Monster.put("Kamikemono", 
        		"```"
        				+ "Description: Mysterious beings that embody the spirits of nature. Kamikemonos are often seen as guardians of the forest and natural world, possessing an ethereal beauty and grace. They can control elements of nature and communicate with animals, serving as intermediaries between humans and the natural world." +"\n"
        		+ "Mystic Evolution: They can summon 3 wolves that have half your stats once per day.\r\n"
        		+ "Ancient Wisdom: Once per day, they can find anyone anywhere in the world and teleport to them."+ "```"+"\n"
        		+ "Help: Doesnt Evolve"
        		);
        
        UserData.Monster.put("Spectre", 
        		"```"
        				+ "Description: Ethereal beings that haunt the living, often bound to the places or objects of their past. Spectres are known for their ghostly appearance and ability to pass through walls." +"\n"
        		+ "Ethereal Form: Once per combat, become incorporeal and immune to physical attacks for 1 turn.\r\n"
        		+ "Haunting Presence: Gain Advantage on Willpower checks when inflicting mental statuses"+ "```"+"\n"
        		+ "Help: Level"
        		);
        
        UserData.Monster.put("Phantom", 
        		"```"
        				+ "Description: More powerful and elusive than spectres, phantoms can manipulate the physical world and instill fear in the living. They are often seen as shadowy figures." +"\n"
        		+ "Spectral Touch: Once per combat, deal necrotic damage equal to your Magic stat (Reducible by willpower only).\r\n"
        		+ "Invisibility: Once per combat, become invisible for 1 turn giving all non-AOE attacks disadvantage towards you."+ "```"+"\n"
        		+ "Help: Alignment + Level"
        		);
        
        UserData.Monster.put("Mystic Angel", 
        		"```"
        				+ "Description: Celestial beings with enhanced magical abilities. Mystic Angels are guardians of sacred knowledge and wield powerful spells to protect the innocent." +"\n"
        		+ "Divine Radiance: Once per day, heal allies 25%.\r\n"
        		+ "Holy Protection: Once per combat, all allies can reroll a roll."+ "```"+"\n"
        		+ "Help: Level"
        		);
        
        UserData.Monster.put("Angel", 
        		"```"
        				+ "Description: Divine messengers of light and goodness. Angels serve as protectors and guides, often intervening to aid those in need and uphold justice." +"\n"
        		+ "Heavenly Presence: Enemies have their stats reduced by half.\r\n"
        		+ "Divine Shield: Once per combat, create a shield that absorbs damage equal to your Magic stat.```" +"\n"+
        		 "Help: Better stay active~"
        	 );
        
        UserData.Monster.put("Fallen", 
        		"```"
        				+ "Description: Once divine beings that have fallen from grace, the Fallen are now shrouded in darkness. They struggle with their lost purity and often seek redemption." +"\n"
        		+ "Dark Resilience: Gain a 1dendurance/2 and 1dmagic/2 to Endurance and Magic when using dark magic." +"\n"
        		+ "Cursed Aura: Enemies take -15 to their rolls."+ "```"
        		+ "Help: RIP"
        		);
        
        UserData.Monster.put("Lesser Daemon", 
        		"```"
        				+ "Description: Minor infernal beings that serve more powerful daemons. Lesser Daemons are known for their mischievous nature and minor dark powers." +"\n"
        		+ "Infernal Affinity: Gain 1dMagic/5 when casting spells\r\n"
        		+ "Demonic Resilience: Reduce incoming damage from magic by 4```" +"\n"
        		+ "Help: :sparkles: Magic :sparkles:"
        		+ "");
        
        UserData.Monster.put("Greater Daemon", 
        		"```"
        				+ "Description: Powerful infernal entities with significant dark magic and strength. Greater Daemons command respect and fear among lesser daemons and mortals alike." +"\n"
        		+ "Hellfire Mastery: They can destroy any magical construct\r\n"
        		+ "Fearsome Presence: Enemies take Disadvantage on their first attack roll." + "```"+"\n"
        		+ "Help: :sparkles: Magic :sparkles:"
        		);

        UserData.Monster.put("Arch Daemon",
        		"```"
        				+ "Description: High-ranking daemons with immense power and influence. Arch Daemons are master manipulators and wield vast dark magic to achieve their goals." +"\n"
        		+ "Abyssal Power: Once per combat, deal additional damage equal to your Magic stat.\r\n"
        		+ "Infernal Command: Once per combat, command an enemy to perform an action (Willpower save to resist)."+ "```"+"\n"
        		+ "Help::sparkles: Magic :sparkles:"
        		);
        
        UserData.Monster.put("Daemon Lord", 
        		"```"
        				+ "Description: Supreme rulers of the infernal realms, Daemon Lords possess unmatched power and command legions of daemons. They are embodiments of pure evil and chaos." +"\n"
        		
        		+ "Dark Dominion: Enemies take Disadvantage on all rolls.\r\n"
        		+ "Hellish Aura: All magic damage is increased by 100% for every person in combat"+ "```"+"\n"
        		+ "Help::sparkles: Magic :sparkles:"
        		);
        
        UserData.Monster.put("Devil Lord", 
        		"```"
        				+ "Description: Similar to Daemon Lords, but with a more structured and lawful approach to their malevolence. Devil Lords are cunning strategists and master deceivers." +"\n"
        		+ "Infernal Wrath: Once per combat, deal maximum damage with a fire spell.\r\n"
        		+ "Unholy Resilience: Immune to fire and necrotic damage."+ "```"+"\n"
        		+ "Help::sparkles: Magic :sparkles:"
        		);
        
        UserData.Monster.put("Lesser Elemental Spirit",
        		"```"
        				+ "Description: Minor elemental beings that embody a specific element. Lesser Elemental Spirits are often guardians of natural sites and have limited elemental abilities." +"\n"
        		+ "Elemental Affinity: Gain +5 when using spells of an element of your choice\r\n"
        		+ "Elemental Resistance: Reduce incoming damage from one chosen element by 20.\r\n"
        		+ "```"+"\n"
        		+ "Help:Level"
        		);
        
        UserData.Monster.put("Medium Elemental Spirit", 
        		"```"
        				+ "Description: More powerful than their lesser counterparts, Medium Elemental Spirits can manipulate their element with greater skill and are often called upon for assistance by mortals." +"\n"
        		+ "Elemental Mastery:Gain an elemental skill slot\r\n"
        		+ "Elemental Burst: Once per combat, deal additional elemental damage equal to your Magic stat/2"+ "```"+"\n"
        		+ "Help: Level"
        		);
        
        UserData.Monster.put("Greater Elemental Spirit", 
        		"```"
        				+ "Description: Highly powerful elemental beings with significant control over their element. Greater Elemental Spirits are revered and often worshipped by those who depend on their element." +"\n"
        		+ "Elemental Form: Attacks against you cause the attacker to take magic/12 unreducible elemental damage.\r\n"
        		+ "Elemental Aura: The element you chose as a lesser elemental has its damage increased by 50%"+ "```"+"\n"
        		+ "Help: Level"
        		);
        
        UserData.Monster.put("Spririt Lord", 
        		"```"
        				+ "Description: Highly powerful elemental beings with significant control over their element. Greater Elemental Spirits are revered and often worshipped by those who depend on their element." +"\n"
        		+ "Elemental Overload: Once per combat, deal maximum damage with an elemental spell and half of it cant be reduced.\r\n"
        		+ "Elemental Shield: Once per combat, create a shield that absorbs damage equal to half your Magic stat and reflects all damage dealt to it until broken."+"```"+"\n"
        		+ "Help: Level/Alignment"
        		 );
        
        UserData.Monster.put("Holy Spirit",
        		"```"
        				+ "Description: Sacred elemental beings blessed with divine light. Holy Elementals combine the power of their element with divine energy, often serving as protectors of the pure and righteous." +"\n"
        		+ "Divine Power: Once per combat, deal unreducible damage equal to half your magic stat as elemental damage.\r\n"
        		+ "Blessed Aura: Allies heal 10% every turn they aren't damaged."+ "```" +"\n"
        		+ "Help: Level"
        		);
        
        UserData.Monster.put("Spirit Royalty", 
        		"```"
        				+ "Description: Noble elemental beings with great authority and power. Elemental Royalty are respected leaders within their elemental domains and possess exceptional control over their element." +"\n"
        		+ "Elemental Sovereignty: Elemental attacks of your choice heal you instead.\r\n"
        		+ "Elemental Command: Once per combat, command an enemy to perform an action (Willpower save to resist). (This cannot cause them harm and must be a simple command)"+ "```"+"\n"
        		+ "Help: Level"
        		);
        
        UserData.Monster.put("Grand Elemental", 
        		"```"
        				+ "Description: The most powerful and ancient of elemental beings. Grand Elementals embody the pure essence of their element and possess unparalleled strength and wisdom." +"\n"
        		+ "Ultimate Elemental Power:Immunity to all elements\r\n"
        		+ "Elemental Majesty: Gain an Ultimate skill. (Little to no limitations and no drawbacks)"+ "```"+"\n"
        		+ "Help: Doesnt Evolve"
        		
        		);
        
        UserData.Monster.put("Lorelei",
        		"```"
        				+ "Description: Enchanting water spirits known for their beauty and melodious voices. Lorelei lure unsuspecting mortals to watery graves or protect sacred waters." +"\n"
        		+ "Mystic Voice: Once per combat, charm an enemy for 1 turn (Willpower save to resist).\r\n"
        		+ "Rock of the River: Immunity to Earth Damage and once per combat, all attacks are considered earth for 1 turn."+ "```"+"\n"
        		+ "Help: Doesnt Evolve"
        		);
        
        UserData.Monster.put("Dryad",
        		"```"
        				+ "Description: Tree spirits that protect forests and natural sanctuaries. Dryads are deeply connected to nature and can control plant life to safeguard their domains." +"\n"
        		+ "Nature’s Blessing: Once per combat, immobilize all combatant 1 turn, making them immune to damage while immobilized.\r\n"
        		+ "Tree Stride: Can hide in trees making them incorporeal, they can also travel in trees."+ "```"+"\n"
        		+ "Help: I speak for the trees!"
        		);
        
        UserData.Monster.put("Dryas", 
        		"```"
        				+ "Description: More powerful and ancient than Dryads, Dryas are guardians of ancient forests and possess greater control over nature. They are revered by all forest creatures." +"\n"
        		+ "Ancient Wisdom: Summons incorporeal forest of 100 trees to fight for them. Each tree attacks with 1dmagic/30 (Minimum 1). If an attack deals damage, they stun the target.\r\n"
        		+ "Nature’s Guardian: Can revive a creature by turning them into a dryad once per week."+ "```"+"\n"
        		+ "Help: Doesnt Evolve"
        		);
        
        UserData.Monster.put("Giant", 
        		"```"
        				+ "Description: Enormous humanoids known for their great strength and size. Giants live in mountainous regions and are formidable opponents in battle." +"\n"
        		+ "Titan’s Strength: Advantage on Strength rolls.\r\n"
        		+ "Mighty Roar: Spends an action to give disadvantage to all enemies for 2 defensive turn."+ "```"+"\n"
        		+ "Help: Level"
        		);
        
        UserData.Monster.put("True Giant", 
        		"```"
        				+ "Description: Ancient and immensely powerful giants with unrivaled strength. True Giants are revered among their kin and often serve as leaders or guardians of their territories." +"\n"
        		+ "Colossal Power: Once per combat, deal maximum damage with an attack.\r\n"
        		+ "Immovable Stance: Reduce incoming damage by full for 1 turn."+ "```" +"\n"
        		+ "Help: Doesnt Evolve"
        		);
        
        UserData.Monster.put("Cyclops", 
        		"```"
        				+ "Description: One-eyed giants known for their brute strength and craftsmanship. Cyclops are skilled blacksmiths and builders, often creating powerful weapons and structures." +"\n"
        		+ "Focused Gaze: Once per combat, gain tier 2 Advantage on an attack roll.\r\n"
        		+ "Brutal Strength: +5 to Strength rolls." + "```"+"\n"
        		+ "Help: Doesnt Evolve"
        		);
        
        UserData.Monster.put("Giant Ogre", 
        		"```"
        				+ "Description: Massive ogres with the strength of giants. Giants Ogres are fearsome warriors, combining the ferocity of ogres with the sheer power of giants." +"\n"
        		+ "Savage Charge: Once per combat, perform a charge attack that deals double damage.\r\n"
        		+ "Thick Hide: Reduce incoming damage by half, once per combat."+ "```"+"\n"
        		+ "Help: Level"
        		);
        
        UserData.Monster.put("Hecatoncheires",
        		"```"
        				+ "Description: Legendary giants with multiple arms, known for their incredible strength and combat prowess. Hecatoncheires are ancient beings that can overwhelm any foe with their numerous limbs and unyielding power." +"\n"
        		+ "Hundred Hands: They can wield up to 10 weapons but lose the ability to wield weapons.\r\n"
        		+ "Unstoppable Force: Once per day, become immune to all damage for 3 turns. If used while unconscious, they wake up regardless of current Health status but pass out once the effect is over."	+ "```"+"\n"
        		+ "Help: Doesnt Evolve"
        	);
        
        UserData.Monster.put("Harpy", 
        		"```"
        				+ "Description: Winged humanoids with the body of a bird and the head of a woman. Harpies are agile and swift, often known for their enchanting songs and ability to control the winds." +"\n"
        		+ "Extreme Flight:  They have Advantage against any target in the air. (Attack and Defense)\r\n"
        		+ "Prideful Talons: Gain a weapon bonus equal to Agility/20"	+ "```"+"\n"
        		+ "Help: Level"
        	);
        
        UserData.Monster.put("Spirit Bird",
        		"```"
        				+ "Description: Ethereal avian creatures that embody the essence of the skies. Spirit Birds are revered as messengers of the gods and possess the ability to manipulate the weather and air currents." +"\n"
        		+ "Spectral Wings: Once per combat, avoid an attack completely by turning ethereal.\r\n"
        		+ "Spirit Song: Allies gain a temporary bonus to their rolls. (1dMagic/4)"+ "```"+"\n"
        		+ "Help: Level"
        		);
        
        UserData.Monster.put("Divine Bird",
        		"```"
        				+ "Description: Celestial birds blessed with divine powers. Divine Birds are symbols of purity and grace, often serving as guardians of sacred places and protectors against evil forces." +"\n"
        		+ "Heavenly Flight: Advantage on all attack and defense rolls against grounded targets.\r\n"
        		+ "Air Dominance: Can force any creature to land if lower Level"+ "```"+"\n"
        		+ "Help: Doesnt Evolve"
        		);
        
        UserData.Monster.put("Wight", 
        		"```"
        				+ "Description: Powerful undead creatures with enhanced strength and intelligence. Wights are more formidable than typical zombies, often retaining some memories and skills from their previous lives." +"\n"
        		+ "Past Life: Gain 1 bonus skill slots\r\n"
        		+ "Skeletal Remains: Strength attacks deal 50% less damage towards them but everything else deals 100% bonus damage"	+ "```"+"\n"
        		+ "Help: Level"
        	);
        
        UserData.Monster.put("WightKing", 
        		"```"
        				+ "Description: A supreme leader among wights, the Wight King commands legions of undead with unmatched authority. They possess immense dark magic and are nearly indestructible." +"\n"
        		+ "King’s Command: Once per combat, command an enemy to perform an action 1dmagic/12 vs willpower.\r\n"
        		+ "Aura of Dread: Humanoids have disadvantage against the user."+ "```"+"\n"
        		+ "Help: Level"
        	);
        
        UserData.Monster.put("Lich", 
        		"```"
        				+ "Description: Undead sorcerers who have achieved immortality through dark rituals. Liches are masters of necromancy and wield powerful magic, often commanding vast armies of undead." +"\n"
        		+ "Necromantic Power: Once per combat, deal additional necrotic damage equal to your Magic stat/2 unreducible .\r\n"
        		+ "Undying Will: Once per day, if reduced to 0 HP, revive with your maximum HP."+ "```"+"\n"
        		+ "Help: 666"
        		);
        
        UserData.Monster.put("True Lich",
        		"```"
        				+ "Description: The pinnacle of lichdom, True Liches are ancient beings with unparalleled magical prowess. They have transcended mortal limitations and are nearly invincible in their pursuit of arcane knowledge."+"\n" 
        		+ "Master of Death: Once per combat, deal maximum damage with a dark magic or necrotic magic skill.\r\n"
        		+ "Phylactery: Immune to death effects; revive after 1d4 turns if reduced to 0 HP with 1 hp."+ "```"+"\n"
        		+ "Help: Doesnt Evolve"
        		);
        
        UserData.Monster.put("Insectar",
        		"```"
        				+ "Description: Intelligent insect-like beings with a hive mind. Insectars are known for their advanced social structures and ability to adapt quickly to any environment, making them formidable opponents." +"\n"
        		+ "Bugs Life: Regens Vitality x2 per hour.\r\n"
        		+ "Rapid Growth: 5x EXP but cannot level up once an evolution is available" 	+ "```"+"\n"
        		+ "Help: Growth"
        	);
        
        UserData.Monster.put("Divine Insect",
        		"```"
        		+ "Description: Sacred insect beings imbued with divine energy. Divine Insects are revered in many cultures and possess unique abilities that set them apart from ordinary insects." +"\n"
        		+ "Swarm Mastery: Once per combat, summon a swarm of insects to attack an enemy  1dmagic vs 1dwillpower\r\n"
        		+ "Chitin Armor: Reduce incoming physical damage by 50% for 1 turn."	+ "```"+"\n"
        		+ "Help: Level"
        	);
        
        UserData.Monster.put("Star Construct Wasp",
        		"```"
        				+ "Description: Mechanical wasps powered by celestial energy. Star Construct Wasps are highly advanced and often serve as guardians or scouts in high-tech civilizations." +"\n"
        		+ "Stellar Sting: Once per combat, deal additional damage equal to your Magic stat with a sting attack that causes magic/2 poison for 3 turns.\r\n"
        		+ "Cosmic Resilience: Once per day, become immune to all damage for 1 turn."	+ "```"+"\n"
        		+ "Help: Doesnt Evolve"
        	);
        
        UserData.Monster.put("Spiritual Doll", 
        		"```"
        				+ "Description: Enchanted dolls brought to life by powerful magic. Spiritual Dolls are often used as guardians or servants, possessing a degree of intelligence and autonomy." +"\n"
        		+ "Spiritual Influence Choose Spirit, Demon, Or Angel for effects in further evolutions\r\n"
        		+ "Resilient Body: Ignore all damage taken for 3 turns but once the 3 turns is up, you take all ignored damage."+ "```"+"\n"
        		+ "Help: Level"
        		);
        
        UserData.Monster.put("Chaos Doll", 
        		"```"
        				+ "Description: Malevolent dolls infused with chaotic energy. Chaos Dolls are unpredictable and dangerous, often causing havoc wherever they go with their chaotic powers." +"\n"
        		+ "Chaotic Influence: Once per combat, have your opponent roll your attack using their stats.\r\n"
        		+ "Extreme Durability: Ignored damage less that 5% of its HP"+ "```"+"\n"
        		+ "Help: Level"
        		);
        
        UserData.Monster.put("Chaos Metalloid",
        		"```"
        				+ "Description: Metallic beings born from chaos energy. Chaos Metalloids are incredibly resilient and possess the ability to manipulate metal and energy to their will." +"\n"
        		+ "Metallic Body: Ignore damage less than 10% of your HP\r\n"
        		+ "Chaotic Energy: Angel: The user heals magic/10 to itself and all allies every successful attack"+"\n"
        		+ "Demon: The user deals magic/10 to all enemies after every successful hit"
        		+ "Spirit: The user deals magic/20 to all enemies and heals magic/20 to all allies after every successful hit"+ "```"+"\n"
        		+ "Help: Level"
        	);
        
        UserData.Monster.put("Arch Doll",
        		"```"
        				+ "Description: The most powerful of animated dolls, Arch Dolls are created with the highest level of magic. They possess immense strength and intelligence, often serving as elite guards or warriors." +"\n"
        		+ "Supreme Command: Once per Combat, 1dmagic/4 vs 1dwillpower. If you succeed, that target is now your ally for the duration of combat or until stunned.\r\n"
        		+ "Extreme Possession: Angel: The user is deals 100% bonus damage against all chaotic or evil aligned characters"	
        	+ "Demon: The user deals 100% bonus damage from good or lawful aligned characters"+"\n"
        		+"Spirit: The user takes 75% less damage from all neutral or chaotic characters"+ "```"+"\n"
        		
        		+ "Help: Doesnt Evolve"
        	);
        
        UserData.Monster.put("Slime", 
        		"```"
        				+ "Description: Amorphous creatures made of gelatinous substance. Slimes are often underestimated due to their simple appearance, but they can be surprisingly resilient and adaptable, capable of absorbing and mimicking various substances." +"\n"
        		+ "Amorphous Form: Once per combat, avoid an attack completely by changing shape.\r\n"
        		+ "Absorption: After taking damage, reduce damage of the same type by 5% (This can stack) resets after 4 turns of no damage from that type.\r\n"
        		+ "Buff Out Dent: Regen 5% hp every turn."	+ "```"+"\n"
        		+ "Help: Doesnt Evolve"
        	);
        
        UserData.Monster.put("Demonoid", 
        		"```"
        				+ "Description: Humans infused with demonic energy, gaining formidable power at the cost of their humanity. They struggle with their inner darkness, often battling between their human and demonic natures." +"\n"
        		+ "Hellish Resilience: 50% resistance to all damage\r\n"
        		+ "Malevolent Presence: Cause Fear (enemies must succeed on a Willpower check or be unable to attack you for 3 turn) once per combat."	+ "```"+"\n"
        		+ "Help: Doesnt Evolve"
        	);
        
        UserData.Monster.put("Dhampir", 
        		"```"
        				+ "Description: A hybrid of a human and a vampire, possessing some vampiric abilities like enhanced strength and longevity, but typically weaker than pure vampires." +"\n"
        		+ "Resilient Strength: Add +magic/20 to all rolls\r\n"
                + "Half Blood: Fire damage deals 100% bonus damage but everything else deals 25% less damage to you."+
                	"```"+"\n"
        		+ "Help: Level");
        
        UserData.Monster.put("Vampire",
        		"```"
        				+ "Description: An immortal creature of the night, known for its supernatural strength, speed, and the ability to drain life energy or blood from its victims." +"\n"
        		+ "Feeding Time: By consuming the flesh of an unwilling or unconscious person, your magic stat is doubled for 4 turns. (Doesnt stack)\r\n"
                + "Vulnerability: Going out during 9 am - 3 pm EST reduces your stats by half" + "\n" +
        		" Resistances: The user is resistant to all damage aside from radiant, holy, fire, or heat by 50% but taking 200% from the listed sources"  + "```"+"\n"
        		+ "Help: Level"
              );
        
        UserData.Monster.put("Surmounter",
        		"```"
        				+ "Description: An advanced and powerful form of vampire, surpassing regular vampires in abilities and often possessing unique, formidable powers." +"\n"
        		+ "Vampiric Royalty: They have 100% increased magic stat when rolling" + "\n"
        		+ "Blood of the Divine: They cant be killed, instead, they regenerate from whatever health they are left at. If they are unconscious for more than 168 hours, they die."+ "```"+"\n"
        		+ "Help: Doesnt Evolve"
        		);
        
        //Surmounter
        
        this.proxyInt=1;
        this.ProxyUserID = "";
        this.Avatar="";
  }
   
    public String getUserId() {
    	return proxyInt==2 ? ProxyUserID : userId;
    }

    public Map<String, Integer> getStats() {
        return stats;
    }
    public Map<String, String> getCdatas() {
        return Cdata;
    }

    public void addStat(String stat, int amount, UserData userdata) {
    	   if (this.stats.get("HP") == null) {
           	this.stats.put("HP", 0);
           }
           if (this.stats.get("Health") == null) {
           	this.stats.put("Health", 0);
           }
    	int newStat = this.stats.getOrDefault(stat,0)+amount;
    	int oldStat = this.stats.getOrDefault(stat,0);
        this.stats.put(stat, newStat);
        
        if (this.stats.get(stat)<0&& stat != "HP") {
        	this.stats.put(stat, 0);
        }
        if (stat == "vitality" || stat == "Health") {
        	this.stats.put("HP", this.stats.get("Health"));
        }
        if (this.stats.get(stat)<-(this.stats.get("Health")) && stat == "HP") {
        	this.stats.put(stat, -((this.stats.get("Health"))));}
        if (stat.equals("vitality"))
        MyListener.updateVitality(userdata, oldStat, newStat);
    }

    public int getStat(String stat) {
    	
        return this.stats.getOrDefault(stat, 0);
        
    }
public String getCdata(String Cdatarequest) {
    	
        return this.Cdata.get(Cdatarequest);
    }

    public void setStat(String stat, int value, UserData userdata) {
    	int placeHolder = userdata.getStat(stat);
        this.stats.put(stat, Math.max(value, 0)); // Ensure stats cannot be negative
        if (this.stats.get(stat)<0&& stat != "HP") {
        	this.stats.put(stat, 0);
        }
        if (stat == "Vitality" || stat == "Health") {
        	this.stats.put("HP", this.stats.get("Health"));
        }
        if (this.stats.get("HP") == null) {
        	this.stats.put("HP", 0);
        }
        if (this.stats.get("Health") == null) {
        	this.stats.put("Health", 0);
        }
        if (this.stats.get(stat)<-(this.stats.get("Health")) && stat == "HP") {
        	this.stats.put(stat, -((this.stats.get("Health"))));}
        if (stat.equals("Health")||stat.equals("HP")||stat.equals("vitality"))
        	if (placeHolder != userdata.getStat(stat)) {
            MyListener.updateVitality(userdata, placeHolder, userdata.getStat(stat));}
        
  
    }
    
    public int getLevelUpPoints() {
        return levelUpPoints;
    }

    public void addLevelUpPoints(int points) {
        this.levelUpPoints += points;
   
    }

    public void subtractLevelUpPoints(int points) {
        this.levelUpPoints = Math.max(this.levelUpPoints - points, 0);
      
    }
   

    public void removeStat(String stat, int amount, UserData userData) {
    	int placeHolder = userData.getStat(stat);
    	
        int currentValue = this.stats.getOrDefault(stat, 0);
        this.stats.put(stat, currentValue - amount);
        if (this.stats.get(stat)<0&& stat != "HP") {
        	this.stats.put(stat, 0);
        }
        if (stat == "vitality" || stat == "Health") {
        	this.stats.put("HP", this.stats.get("Health"));
        	
        }
        
        if (this.stats.get(stat)<-(this.stats.get("Health")) && stat == "HP") {
        	this.stats.put(stat, -((this.stats.get("Health"))));}
        if (stat.equals("Health")||stat.equals("vitality"))
        	
            MyListener.updateVitality(userData, placeHolder, userData.getStat(stat));
        
     
    }
  

}