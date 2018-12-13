package populate;
import java.sql.SQLException;


public class populate 
{

	public static void main(String[] args) throws SQLException 
	{
		// TODO Auto-generated method stub
                
		/*populate_user a = new populate_user();
		System.out.println("POPULATING USER NOW!!!");
 		a.run_user();
               */
		populate_business b = new populate_business();
		System.out.println("POPULATING BUSINESS NOW!!!");
		b.run_business();
		
                /*populate_checkin c = new populate_checkin();
		System.out.println("POPULATING CHECKIN NOW!!!");
		c.run_checkin();
                populate_review d = new populate_review();
		System.out.println("POPULATING REVIEW NOW!!!");
		d.run_review();
                */

	}

}
