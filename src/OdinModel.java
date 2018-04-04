import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.StringTokenizer;

public class OdinModel
{
	//I'm being told that this needs to be static, anyone know why?
	final String FILENAME = "ServerInfo.txt";
	
	public static void main(String[] args) throws Exception
	{
		OdinModel OM = new OdinModel();
		OM.appMain(args);
	}
	
	public void appMain(String[] args)
	{
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		try
		{
			myConn = connect();
			myStmt = myConn.createStatement();
			if(args.length > 0) myRs = myStmt.executeQuery(args[0]);
			while(myRs.next()) System.out.println(myRs.getString("Name"));
			myConn.close();
			myStmt.close();
			myRs.close();
		}catch (Exception e){ e.printStackTrace(); }//System.out.println("Failure."); 
	}
	
	public Connection connect() throws Exception
	{
		StringTokenizer stk;
		Connection con;
		BufferedReader reader;
		reader = new BufferedReader(new FileReader(FILENAME));
		stk = new StringTokenizer(reader.readLine(), "\t");
		con = DriverManager.getConnection(stk.nextToken(), stk.nextToken(), stk.nextToken());
		reader.close();
		return con;
	}
}
