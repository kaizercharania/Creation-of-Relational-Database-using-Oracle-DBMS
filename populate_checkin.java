package populate;

import java.io.BufferedReader;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class populate_checkin 
{

	public void run_checkin() throws SQLException
	{
		// TODO Auto-generated method stub
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		String sqlquery = "INSERT INTO check_in" 
		+ "(ci_day, ci_hour, ci_count, bid) VALUES" 
				+ "(?,?,?,?)";
		
		JSONParser parser = new JSONParser();
		try
		{
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(sqlquery);
			FileReader filereader = new FileReader("yelp_checkin.json");
			
			BufferedReader bufferedReader = new BufferedReader(filereader);
			String line;
			while ((line = bufferedReader.readLine()) != null) 
			{
				Object obj = parser.parse(line);
				JSONObject jsonObject = (JSONObject) obj;
				
				String business_id = (String) jsonObject.get("business_id");
				preparedStatement.setString(4, business_id);
                                //System.out.println("business:" + business_id );
				
				JSONObject jsonObject2 = (JSONObject) jsonObject.get("checkin_info");
				String ci_day;
				int ci_hour;
				int ci_count;
				
				for (Object key : jsonObject2.keySet()) 
				{
			        String keyStr = (String)key;
			        Object keyvalue = jsonObject2.get(keyStr);
			        ci_day = convert_day(keyStr);
			        ci_hour = convert_hour(keyStr);
			        ci_count = ((Long) keyvalue).intValue();
			        
			        preparedStatement.setString(1, ci_day);
                                //System.out.println("ci_day" + ci_day );
			        preparedStatement.setInt(2, ci_hour);
                                //System.out.println("ci_hour" + ci_hour );
			        preparedStatement.setInt(3, ci_count);
                                //System.out.println("ci_count" + ci_count );
			        preparedStatement.executeUpdate();
			    }
				
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
			
			if (dbConnection != null) 
			{
				dbConnection.close();
			}
		}

	}
	
	public static String convert_day(String a)
	{
		String[] b = a.split("-");
		String c = "WRONG ENTRY!!!";
		
		if (b[1].equals("0"))
		{
			c = "SUNDAY";
		}
		if (b[1].equals("1"))
		{
			c = "MONDAY";
		}
		if (b[1].equals("2"))
		{
			c = "TUESDAY";
		}
		if (b[1].equals("3"))
		{
			c = "WEDNESDAY";
		}
		if (b[1].equals("4"))
		{
			c = "THURSDAY";
		}
		if (b[1].equals("5"))
		{
			c = "FRIDAY";
		}
		if (b[1].equals("6"))
		{
			c = "SATURDAY";
		}
		
		return c;
	}
	
	public static int convert_hour(String a)
	{
		String[] b = a.split("-");
		int c;
		c = Integer.parseInt(b[0]);
		return c;
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
