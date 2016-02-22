/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import java.util.Stack;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author tabramishvili
 */
public class MultiSceneStage extends Stage{

    private final Stack<Scene> scenes;

    /**
     * მრავალსცენიანი stage, რომელზეც მარტივია სცენების შეცვლა სტეკური პრინციპით
     */
    public MultiSceneStage() {
        super();
        scenes = new Stack<>();
    }

    /**
     * ახალი სცენა დაემატება სტეკის თავში და თან გამოჩნდება ეკრანზე
     * @param scene 
     */
    public void addScene(Scene scene) {
        super.setScene(scene);
        scenes.push(scene);
    }
    
    /**
     * თიკი სტეკში გვაქვს 2 ან მეტი სცენა, მიმდინარე სცენა ამოიშლება და წინა სცენა გამოჩნდება ეკრანზე
     * @return boolean - მოხდა თუ არა სცენის შეცვლა
     */
    public boolean goBack(){
        if (scenes.size() < 2)
            return false;
        scenes.pop();
        setScene(scenes.peek());
        return true;
    }
    
}
