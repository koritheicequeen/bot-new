package mybotproject;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

public class ButtonClickListener extends ListenerAdapter {
    public static List<Button> createButtons(List<String> EvolutionName) {
        List<Button> buttons = new ArrayList<>();
        for (String evolution : EvolutionName) {
        buttons.add(Button.secondary(evolution, evolution));
        }
        return buttons;
       
    }
    public static List<Button> checkYes(String userId, MessageReceivedEvent event) {
        List<Button> buttons = new ArrayList<>();
        
        
        buttons.add(Button.primary(userId + "@", "Yes"));
        buttons.add(Button.secondary("No", "No"));
        
        
        return buttons;
       
    }
    public static List<Button> nextPage(MessageReceivedEvent event) {
        List<Button> buttons = new ArrayList<>();
        
       
        buttons.add(Button.secondary("Previous Page", "Previous Page"));
        buttons.add(Button.secondary("Next Page", "Next Page"));
        
        
        return buttons;
    }
}
