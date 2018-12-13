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

public class populate_business {

	public void run_business() throws SQLException
	{
		// TODO Auto-generated method stub
		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement2 = null;
		PreparedStatement preparedStatement3 = null;
		PreparedStatement preparedStatement4 = null;
		PreparedStatement preparedStatement5 = null;
		PreparedStatement preparedStatement6 = null;

		String sqlquery = "INSERT INTO BUSINESS" 
		+ "(bid, full_address, open_now, city, review_count, b_name, longitude, state, stars, latitude, b_type) VALUES" 
				+ "(?,?,?,?,?,?,?,?,?,?,?)";
		String sqlquery2 = "INSERT INTO b_hours" + "(d_o_w, from_h, to_h, bid) VALUES" + "(?,?,?,?)";
		String sqlquery3 = "INSERT INTO b_main_category" + "(c_name, bid) VALUES" + "(?,?)";
		String sqlquery4 = "INSERT INTO b_sub_category" + "(c_name, bid) VALUES" + "(?,?)";
		String sqlquery5 = "INSERT INTO neighborhoods" + "(n_name, bid) VALUES" + "(?,?)";
		String sqlquery6 = "INSERT INTO b_attributes" + "(a_name, bid) VALUES" + "(?,?)";
		
		JSONParser parser = new JSONParser();
		
		try
		{
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(sqlquery);
			preparedStatement2 = dbConnection.prepareStatement(sqlquery2);
			preparedStatement3 = dbConnection.prepareStatement(sqlquery3);
			preparedStatement4 = dbConnection.prepareStatement(sqlquery4);
			preparedStatement5 = dbConnection.prepareStatement(sqlquery5);
			preparedStatement6 = dbConnection.prepareStatement(sqlquery6);
			FileReader filereader = new FileReader("yelp_business.json");
			
			BufferedReader bufferedReader = new BufferedReader(filereader);
			String line;
                        int number = 0;
			while ((line = bufferedReader.readLine()) != null) 
			{
				System.out.println(number);
                                number++;
                                Object obj = parser.parse(line);
				JSONObject jsonObject = (JSONObject) obj;
				
				String business_id = (String) jsonObject.get("business_id");
				preparedStatement.setString(1, business_id);
				
				String b_address = (String) jsonObject.get("full_address");
				preparedStatement.setString(2, b_address);
				
				boolean open_now = (Boolean) jsonObject.get("open");
				int o_now;
				if (open_now)
				{
					o_now = 1;
				}
				else
				{
					o_now = 0;
				}
				preparedStatement.setInt(3, o_now);
				
				String city = (String) jsonObject.get("city");
				preparedStatement.setString(4, city);
				
				int review_count = ((Long) jsonObject.get("review_count")).intValue();
				preparedStatement.setInt(5, review_count);
				
				String b_name = (String) jsonObject.get("name");
				preparedStatement.setString(6, b_name);
				
				float longitude = ((Double) jsonObject.get("longitude")).floatValue();
				preparedStatement.setFloat(7, longitude);
				
				String state = (String) jsonObject.get("state");
				preparedStatement.setString(8, state);
				
				float stars = ((Double) jsonObject.get("stars")).floatValue();
				preparedStatement.setFloat(9, stars);
				
				float latitude = ((Double) jsonObject.get("latitude")).floatValue();
				preparedStatement.setFloat(10, latitude);
				
				String type = (String) jsonObject.get("type");
				preparedStatement.setString(11, type);
				
				//preparedStatement.executeUpdate();
				
				
				
				
				if(jsonObject.get("neighborhoods")!=null)
				{
					JSONArray nei_array = (JSONArray) jsonObject.get("neighborhoods");
					Iterator<String> iterator = nei_array.iterator();
					String n_name;
			
					while(iterator.hasNext())
					{
						n_name = iterator.next();
						preparedStatement5.setString(1, n_name);
						preparedStatement5.setString(2, business_id);
						preparedStatement5.executeUpdate();
					}
				}
				
				JSONArray cat_array = (JSONArray) jsonObject.get("categories");
				Iterator<String> iterator = cat_array.iterator();
				String cat;
		
				while(iterator.hasNext())
				{
					cat = iterator.next();
					if(cat.equals("Active Life") || cat.equals("Arts & Entertainment") || cat.equals("Automotive") || 
							cat.equals("Car Rental") || cat.equals("Cafes") || cat.equals("Beauty & Spas") || 
							cat.equals("Convenience Stores") || cat.equals("Dentists") || cat.equals("Doctors") ||
							cat.equals("Drugstores") || cat.equals("Department Stores") || cat.equals("Education") ||
							cat.equals("Event Planning & Services") || cat.equals("Flowers & Gifts") || 
							cat.equals("Food") || cat.equals("Health & Medical") || cat.equals("Home Services") ||
							cat.equals("Home & Garden") || cat.equals("Hospitals") || cat.equals("Hotels & Travel") ||
							cat.equals("Hardware Stores") || cat.equals("Grocery") || cat.equals("Medical Centers") ||
							cat.equals("Nurseries & Gardening") || cat.equals("Nightlife") || cat.equals("Restaurants") ||
							cat.equals("Shopping") || cat.equals("Transportation"))
					{
						preparedStatement3.setString(1, cat);
						preparedStatement3.setString(2, business_id);
						preparedStatement3.executeUpdate();
					}
					else
					{
						preparedStatement4.setString(1, cat);
						preparedStatement4.setString(2, business_id);
						preparedStatement4.executeUpdate();
					}
					
				}
				
				if(jsonObject.get("attributes")!=null)
				{
					JSONObject jsonObject4 = (JSONObject) jsonObject.get("attributes");
					for (Object key : jsonObject4.keySet()) 
					{
						String keyStr = (String)key;
				        Object keyvalue = jsonObject4.get(keyStr);				      

				        if (keyvalue instanceof JSONObject)
				        {
				        	JSONObject jsonObject5 = (JSONObject) jsonObject4.get(key);
				        	for (Object key2 : jsonObject5.keySet())
				        	{
				        		String keyStr2 = (String)key2;
				        		Object keyvalue2 = jsonObject5.get(keyStr2);
				        		if (keyvalue2 instanceof Integer)
				        		{
				        			String a_value = ((Long) jsonObject5.get(keyStr2)).toString();
				        			keyStr2 = keyStr2 + "_" + a_value;
									preparedStatement6.setString(1, keyStr2);
									preparedStatement6.setString(2, business_id);
									preparedStatement6.executeUpdate();
				        		}
				        		else if (keyvalue2 instanceof String)
				        		{
				        			String a_value = (String) jsonObject5.get(keyStr2);
				        			keyStr2 = keyStr2 + "_" + a_value;
									preparedStatement6.setString(1, keyStr2);
									preparedStatement6.setString(2, business_id);
									preparedStatement6.executeUpdate();
				        		}
				        		else if (keyvalue2 instanceof Boolean)
				        		{
				        			boolean a = (Boolean) jsonObject5.get(keyStr2);
				        			String a_value = String.valueOf(a);
				        			keyStr2 = keyStr2 + "_" + a_value;
									preparedStatement6.setString(1, keyStr2);
									preparedStatement6.setString(2, business_id);
									preparedStatement6.executeUpdate();
				        		}
				        	}
				        }
				        else
				        {
				        	if (keyvalue instanceof Integer)
			        		{
			        			String a_value = ((Long) jsonObject4.get(keyStr)).toString();
			        			keyStr = keyStr + "_" + a_value;
								preparedStatement6.setString(1, keyStr);
								preparedStatement6.setString(2, business_id);
								preparedStatement6.executeUpdate();
			        		}
			        		else if (keyvalue instanceof String)
			        		{
			        			String a_value = (String) jsonObject4.get(keyStr);
			        			keyStr = keyStr + "_" + a_value;
								preparedStatement6.setString(1, keyStr);
								preparedStatement6.setString(2, business_id);
								preparedStatement6.executeUpdate();
			        		}
			        		else if (keyvalue instanceof Boolean)
			        		{
			        			boolean a = (Boolean) jsonObject4.get(keyStr);
			        			String a_value = String.valueOf(a);
			        			keyStr = keyStr + "_" + a_value;
								preparedStatement6.setString(1, keyStr);
								preparedStatement6.setString(2, business_id);
								preparedStatement6.executeUpdate();
			        		} 
				        }
				        
				    }
				}
				
				if(jsonObject.get("hours")!=null)
				{
					JSONObject jsonObject2 = (JSONObject) jsonObject.get("hours");
					for (Object key : jsonObject2.keySet()) 
					{
				        String keyStr = (String)key;				        
				        JSONObject jsonObject3 = (JSONObject) jsonObject2.get(keyStr);
				        String open_h = (String) jsonObject3.get("open");
				        Float o_h = convert_hour(open_h);
				        String close_h = (String) jsonObject3.get("close");
				        Float c_h = convert_hour(close_h);
				        preparedStatement2.setString(1, keyStr);
				        preparedStatement2.setFloat(2, o_h);
					    preparedStatement2.setFloat(3, c_h);
				        preparedStatement2.setString(4, business_id);
				        preparedStatement2.executeUpdate();				       				    
				    }
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
			if (preparedStatement2 != null) 
			{
				preparedStatement2.close();
			}
			if (preparedStatement3 != null) 
			{
				preparedStatement3.close();
			}
			if (preparedStatement4 != null) 
			{
				preparedStatement4.close();
			}
			if (preparedStatement5 != null) 
			{
				preparedStatement5.close();
			}
			if (preparedStatement6 != null) 
			{
				preparedStatement6.close();
			}

			if (dbConnection != null) 
			{
				dbConnection.close();
			}
		}

	}
	
	public static Float convert_hour(String a)
	{
		String[] b = a.split(":");
		float c = Float.parseFloat(b[0]);
		float d = Float.parseFloat(b[1]);
		d = d / 100;
		c = c + d;
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