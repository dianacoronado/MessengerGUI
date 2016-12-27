package messengerapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class Main extends Application{
	
	
	private static List<String> users= new ArrayList<String>();
	private TextArea chatText= new TextArea("Press + to Add new contacts\nPress - to Remove current contacts");
	private File file;

	//main class to launch program
	public static void main(String... args)
	{
		//Adds 10 users, as listed below, users can be deleted or added 
		/**for(int i=1;i<11;i++)
		{
			users.add("User"+i);
		}*/
		
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws FileNotFoundException
	{
		
		
		
		primaryStage.setTitle("Messenger"); // Sets title of window
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
		GridPane root = new GridPane();
		
		
		root.setStyle("-fx-font-size: 11pt;-fx-font-family: Verdana;-fx-text-fill: #422D3D;");
		
		//HBox Text Field and Send button, also binding properties
		HBox textPane= new HBox();
        Button send=new Button("Send");
        TextArea text = new TextArea();
        send.prefHeightProperty().bind(textPane.heightProperty());
        text.prefHeightProperty().bind(textPane.heightProperty());
        send.prefWidthProperty().bind(textPane.widthProperty().subtract(text.widthProperty()));
        textPane.getChildren().addAll(text,send);
        
        
        
        //Pane for list view
        VBox listPane = new VBox();
        ListView<String> userList = new ListView<>(FXCollections.observableArrayList(users));
        listPane.getChildren().add(userList);
        userList.prefHeightProperty().bind(listPane.heightProperty());
        userList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); //Selection is set to single because only file can be opened at a time
        userList.setStyle("-fx-control-inner-background: #73C1C6;");
   
       //Pane to add user buttons, add delete buttons
        HBox userControl = new HBox();
        Button add = new Button("+");
        Button remove= new Button("-");
        userControl.getChildren().addAll(add,remove); 
        add.prefHeightProperty().bind(userControl.heightProperty());
        remove.prefHeightProperty().bind(userControl.heightProperty());
        add.prefWidthProperty().bind(userControl.widthProperty().divide(2));
        remove.prefWidthProperty().bind(userControl.widthProperty().divide(2));
        
        //Set the Text Area to view only
        chatText.setEditable(false);
       
        
        //Addidng the panes to the root Gridpane
        root.add(textPane, 1, 1);
        root.add(userControl, 0, 1);
        root.add(listPane, 0, 0);
        root.add(chatText, 1, 0);
        
        //Stylng for the buttons
        send.setStyle("-fx-background-color: #A79AB2;-fx-border-color:#B57BA6;");
        add.setStyle("-fx-background-color: #A79AB2;-fx-border-color:#B57BA6;");
        remove.setStyle("-fx-background-color: #A79AB2;-fx-border-color:#B57BA6;");
        
        //Binding properties so the different sub=panes are displayed correctly
        textPane.prefWidthProperty().bind(root.widthProperty());
        textPane.prefHeightProperty().bind(root.heightProperty().divide(5));
        listPane.prefHeightProperty().bind(root.heightProperty());
        
        
        
        /**
         * The follow is the controls when a name is selected. I nested the send button action event
         * here because I did not want the button to work unless a name was selected
         */
        userList.getSelectionModel().selectedItemProperty().addListener(ov -> 
        {
        	StringBuilder fileName=new StringBuilder("");
        	
        	//String builder checks to make sure a name is selected
        	if(userList.getSelectionModel().getSelectedItem()!=null)
        	{
        		fileName.append(userList.getSelectionModel().getSelectedItem());
        		fileName.append(".txt");
        	}
 
    		String conversation;
    		BufferedReader reader;
    		chatText.clear();
    		Date today=new Date();
    		
    		//Default Date format for message time stamp
    		SimpleDateFormat format= new SimpleDateFormat();
    		String timestamp=format.format(today);
    		
    		//This looks to see if a file is already created, if it is, then it appends it. If a file has not been created, it creates one
    		try
    		{
    			file = new File("C:\\csc360\\CORONADOD1\\"+ fileName.toString());
    			if(!file.exists())
    			{
    				file.createNewFile();
    			}
    			
    			//If the file exists, it will read the file and display it in the chat box
    			reader = new BufferedReader(new FileReader(file));
    			while ((conversation = reader.readLine()) != null) {
    				chatText.appendText(conversation+"\n");
    			}
    			
    			
    			//If the Send button it's pressed, it will gathered the string from the text area, write it to the file and diplay it in the chat box
    			send.setOnAction(e ->
    			{
    				BufferedWriter writer = null;
    				try 
    				{
						writer = new BufferedWriter(new FileWriter(file,true));
						writer.write(timestamp+": "+text.getText()+"\n");
						writer.newLine();
						chatText.appendText(timestamp+": "+text.getText()+"\n\n");
						text.clear();
						writer.close();
					} 
    				catch (IOException e1) 
    				{
						e1.printStackTrace();
					}
    				
    			});}
    			
    			
    		catch(IOException e)
    		{
    			e.getMessage();
    		}
        
        });
        
        
        
        //Controls for adding more users
        add.setOnAction(e ->{
        		
        		//Add User window components
                Stage stage = new Stage();
                HBox addPane=new HBox();
                Label label = new Label(" Enter a name:");
                TextField newUserText = new TextField();
                Button addUserButton= new Button("Add");
                
                //Binding properties
                newUserText.prefHeightProperty().bind(addPane.heightProperty());
                addUserButton.prefHeightProperty().bind(addPane.heightProperty());
                addUserButton.prefWidthProperty().bind(addPane.widthProperty().divide(3));
                label.prefHeightProperty().bind(addPane.heightProperty());
                label.prefWidthProperty().bind(addPane.widthProperty().divide(3));
                
                addPane.getChildren().addAll(label,newUserText,addUserButton);
                stage.setScene(new Scene(addPane,300,50));
                
                //Styled the button to reflect the main messenger window
                //addPane.setStyle("-fx-font-size: 11pt;-fx-font-family: Verdana;-fx-text-fill: #422D3D;");
                addUserButton.setStyle("-fx-background-color: #A79AB2;-fx-border-color:#B57BA6;");
                stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
                stage.show();
                
                //When the Add button is pressed, it adds the text to the main ArrayList, updates the user list and closes the stage
                addUserButton.setOnAction(i ->
                {
                	users.add(newUserText.getText());
                	userList.setItems(FXCollections.observableArrayList(users));
                	stage.close();
                });
            }
        );
        
        
        
        
        //Removes the selected user from the ArrayList of users, but does not delete the file.
        //When the list is empty, the chatText reverts back to the instructional message
        remove.setOnAction(e ->
        {
        		
        	users.remove(userList.getSelectionModel().getSelectedItem());
        	userList.setItems(FXCollections.observableArrayList(users));
        	if(users.isEmpty())
            {
            	chatText.setText("Press + to Add new contacts");
            }
        });
        
        
		primaryStage.setScene(new Scene(root, 750, 500));
        primaryStage.show();
	}
	
	
}

