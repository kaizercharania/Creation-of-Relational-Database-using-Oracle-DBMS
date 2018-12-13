package populate;


import java.io.BufferedReader;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class populate_user 
{
	public void run_user() throws SQLException
	{
                System.out.println("check 1");
		// TODO Auto-generated method stub
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement2 = null;
		PreparedStatement preparedStatement3 = null;

		String sqlquery = "INSERT INTO YELP_USER" 
		+ "(yelping_since, funny_votes, useful_votes, cool_votes, review_count, user_name, user_id, fans, average_stars, u_type, hot_compliment, more_compliment, profile_compliment, cute_compliment, list_compliment, note_compliment, plain_compliment, cool_compliment, funny_compliment, writer_compliment, photos_compliment ) VALUES" 
				+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String sqlquery2 = "INSERT INTO FRIENDS" + "(user_id, friend_id) VALUES" + "(?,?)";
		String sqlquery3 = "INSERT INTO ELITE_YEARS" + "(user_id, elite) VALUES" + "(?,?)";


		
		JSONParser parser = new JSONParser();
		
		try
		{
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(sqlquery);
			preparedStatement2 = dbConnection.prepareStatement(sqlquery2);
			preparedStatement3 = dbConnection.prepareStatement(sqlquery3);
			FileReader filereader;
                        filereader = new FileReader("yelp_user.json");
			
                        System.out.println("check 2");
                        
			BufferedReader bufferedReader = new BufferedReader(filereader);
			String line;
			while ((line = bufferedReader.readLine()) != null) 
			{
                                System.out.println("check 3");
				Object obj = parser.parse(line);
				JSONObject jsonObject = (JSONObject) obj;
				String yelp_since = (String) jsonObject.get("yelping_since");
				preparedStatement.setString(1, yelp_since);
			
				int review_count = ((Long) jsonObject.get("review_count")).intValue();
				preparedStatement.setInt(5, review_count);
			
				String name = (String) jsonObject.get("name");
				preparedStatement.setString(6, name);
			
				String user_id = (String) jsonObject.get("user_id");
				preparedStatement.setString(7, user_id);
			
				int fans = ((Long) jsonObject.get("fans")).intValue();
				preparedStatement.setInt(8, fans);
			
				float avg_stars = ((Double) jsonObject.get("average_stars")).floatValue();
				preparedStatement.setFloat(9, avg_stars);
			
				String type = (String) jsonObject.get("type");
				preparedStatement.setString(10, type);
				
				JSONObject votes = (JSONObject) jsonObject.get("votes");
				int  funny_votes = ((Long) votes.get("funny")).intValue();
				int  useful_votes = ((Long) votes.get("useful")).intValue();
				int  cool_votes = ((Long) votes.get("cool")).intValue();
				
				preparedStatement.setInt(2, funny_votes);
				preparedStatement.setInt(3, useful_votes);
				preparedStatement.setInt(4, cool_votes);
				
				
				JSONObject compliments = (JSONObject) jsonObject.get("compliments");
				
                            System.out.println("check 4");
                            int hot_compliment;
			    int more_compliment;
			    int profile_compliment;
			    int cute_compliment;
			    int list_compliment;
			    int note_compliment;
			    int plain_compliment;
			    int cool_compliment;
			    int funny_compliment;
			    int writer_compliment;
			    int photos_compliment;
			    if(compliments.get("hot")!=null)
			    {
			    	hot_compliment = ((Long) compliments.get("hot")).intValue();
			    }
			    else
			    {
			    	hot_compliment = 0;	    	
			    }
			    
			    if(compliments.get("more")!=null)
			    {
			    	more_compliment = ((Long) compliments.get("more")).intValue();
			    }
			    else
			    {
			    	more_compliment = 0;	    	
			    }
				
			    if(compliments.get("profile")!=null)
			    {
					profile_compliment = ((Long) compliments.get("profile")).intValue();
			    }
			    else
			    {
			    	profile_compliment = 0;	    	
			    }
			    
			    if(compliments.get("cute")!=null)
			    {
					cute_compliment = ((Long) compliments.get("cute")).intValue();
			    }
			    else
			    {
			    	cute_compliment = 0;	    	
			    }
				
			    if(compliments.get("list")!=null)
			    {
					list_compliment = ((Long) compliments.get("list")).intValue();
			    }
			    else
			    {
			    	list_compliment = 0;	    	
			    }
			    
			    if(compliments.get("note")!=null)
			    {
					note_compliment = ((Long) compliments.get("note")).intValue();
			    }
			    else
			    {
			    	note_compliment = 0;	    	
			    }
			    
			    if(compliments.get("plain")!=null)
			    {
					plain_compliment = ((Long) compliments.get("plain")).intValue();
			    }
			    else
			    {
			    	plain_compliment = 0;	    	
			    }
			    
			    if(compliments.get("cool")!=null)
			    {
					cool_compliment = ((Long) compliments.get("cool")).intValue();
			    }
			    else
			    {
			    	cool_compliment = 0;
			    }
			    
			    if(compliments.get("funny")!=null)
			    {
					funny_compliment = ((Long) compliments.get("funny")).intValue();
			    }
			    else
			    {
			    	funny_compliment = 0;	    	
			    }
			    
			    if(compliments.get("writer")!=null)
			    {
					writer_compliment = ((Long) compliments.get("writer")).intValue();
			    }
			    else
			    {
			    	writer_compliment = 0;	    	
			    }
			    
			    if(compliments.get("photos")!=null)
			    {
					photos_compliment = ((Long) compliments.get("photos")).intValue();
			    }
			    else
			    {
			    	photos_compliment = 0;	    	
			    }
				System.out.println("check 5");
				preparedStatement.setInt(11, hot_compliment);
				preparedStatement.setInt(12, more_compliment);
				preparedStatement.setInt(13, profile_compliment);
				preparedStatement.setInt(14, cute_compliment);
				preparedStatement.setInt(15, list_compliment);
				preparedStatement.setInt(16, note_compliment);
				preparedStatement.setInt(17, plain_compliment);
				preparedStatement.setInt(18, cool_compliment);
				preparedStatement.setInt(19, funny_compliment);
				preparedStatement.setInt(20, writer_compliment);
				preparedStatement.setInt(21, photos_compliment);
				System.out.println("check 6a");
				preparedStatement.executeUpdate();
                                System.out.println("check 6");

				if(jsonObject.get("friends")!=null)
				{
					JSONArray friendarray = (JSONArray) jsonObject.get("friends");
					Iterator<String> iterator = friendarray.iterator();
					String friend_id;
			
					while(iterator.hasNext())
					{
						friend_id = iterator.next();
						preparedStatement2.setString(1, user_id);
						preparedStatement2.setString(2, friend_id);
						preparedStatement2.executeUpdate();
					}
				}
                                System.out.println("check 7");
				if(jsonObject.get("elite")!=null)
				{
					JSONArray elitearray = (JSONArray) jsonObject.get("elite");
					Iterator<Long> iterator2 = elitearray.iterator();
					int elite_year;
				
					while(iterator2.hasNext())
					{
						elite_year = (iterator2.next()).intValue();
						preparedStatement3.setString(1, user_id);
						preparedStatement3.setInt(2, elite_year);
						preparedStatement3.executeUpdate();
					}
				}
                                System.out.println("check 8");
			}
			filereader.close();
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(ParseException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e) 
		{

			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally 
		{
			if (preparedStatement != null) 
			{
				preparedStatement.close();
			}
			if (preparedStatement2 != null) 
			{
				preparedStatement2.close();
			}
			if (preparedStatement3 != null) 
			{
				preparedStatement3.close();
			}

			if (dbConnection != null) 
			{
				dbConnection.close();
			}
		}

	}
	
	public static Connection getDBConnection() 
	{

		Connection dbConnection = null;

		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {

			System.out.println(e.getMessage());

		}

		try {

			dbConnection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:yelp", "scott", "tiger");
			return dbConnection;

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		}

		return dbConnection;

	}		

}
