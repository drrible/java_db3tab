import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DBHelper  extends SQLiteOpenHelper {
	final String LOG_TAG = "myLogs";
	String [] FieldsN;	// fields names
	int numberOfFields;
	String [] FieldsT;
	String DBname;
	String tableN;
	SQLiteDatabase db;
		public DBHelper(Context context,String dbname) {
	    		 super(context, dbname, null, 1);
		 this.DBname=dbname;
	  	   	    }
	public DBHelper(Context context,SQLiteDatabase db,String tableN,
			String [] FieldsN, String [] FieldsT,int nf) {
      super(context, "myDBM", null, 1);
   this.numberOfFields=nf;
   this.FieldsN=FieldsN;
   this.FieldsT=FieldsT;
   this.tableN=tableN;
   this.db=db;
    }
      public void createT(SQLiteDatabase db,String tableN,String [] FieldsN,
    		String [] FieldsT, int numberOfFields)
    {
    	String ss="create table " + tableN + "(";
    	for (int i=0; i<numberOfFields-1;i++)
    		ss=ss+ FieldsN[i] + "  " + FieldsT[i] + ", ";
    	ss=ss+ FieldsN[numberOfFields-1] + "  " + FieldsT[numberOfFields-1];
    ss=ss+");";
    Log.d("Create Table:", tableN+ "  :  "+ ss);
    db.execSQL(ss);
    Log.d("a fost creat", "tabelul"+tableN+ "  ,"+ss);
    }
   public void onCreate(SQLiteDatabase db) {
      Log.d(LOG_TAG, "--- onCreate database ---");
   }
 	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		}
  }





package com.example.iliec.db3tabst;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
public class FileOper {
String tablesN;
int numberOfTables;
public FileOper()
{     }
public FileOper(String tablesN)
{
	this.tablesN=tablesN;
}
public FileOper(int n,String tablesN)
{
this.tablesN=tablesN;
this.numberOfTables=n;
}
public  String readTable(String  tablesN)
{	String aDataRow = "";
	 String aBuffer = "";
	 final String rez;
	{
		try {
			//File myFile = new File("/storage/extSdCard/"+tablesN+".txt");
			File myFile = new File("/mnt/sdcard/"+tablesN+".txt");
	FileInputStream fIn = new FileInputStream(myFile);
	BufferedReader myReader = new BufferedReader(
			new InputStreamReader(fIn));
		while ((aDataRow = myReader.readLine()) != null) {
		aBuffer += aDataRow + "\n";
			}
		myReader.close();
		}
		catch (Exception e) {
		Log.d("ERROR","Error on reading the file"+ tablesN);
			}
		}
			return aBuffer;
	}
}




package com.example.iliec.db3tabst;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.text.DecimalFormat;

public class MainActivity extends Activity implements View.OnClickListener{
	String DBName="CatPrSales3";
	TextView tvName;Button btnName;
	  public DecimalFormat df = new DecimalFormat("#.##");
	String [] data2=new String[50];
	  public String[] sqlprop;
	 	  EditText et1,et2,et3;
	  EditText txtData;
		Button btnWriteSDFile, btnReadSDFile, btnClearScreen,btnClose;
		FileOper tabel;   SQLiteDatabase db;
	TextView tv;
	 DBHelper dbHelper;
	 String [] tableFields;
	String [][] cc=new String[20][20];
	  String [][] cc1=new String[20][20];
	  String [] ss, whereCV;
	  String [] tablesContent=new String[20];
	  String [] tablesStruct=new String[20];
	  String [] tabNames=new String[20];
	  String tabStruct,tabContent;
	  int numberOfTables;
	  public final int IPC_ID = 1122;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtData = (EditText) findViewById(R.id.txtData);
    	txtData.setHint("Enter some lines of data here...");
    	tv = (TextView)findViewById(R.id.textView1);
    	et1=(EditText)findViewById(R.id.editText1);
    	et2=(EditText)findViewById(R.id.editText2);
    	et3=(EditText)findViewById(R.id.editText3);
		btnName = (Button) findViewById(R.id.ReadWds);
		btnName.setOnClickListener(this);
		tv.setMovementMethod(new ScrollingMovementMethod());
                   String whereC="  NF > ?  OR PF=? ";
                   whereC="  PPrice > ?  OR Squant> ? ";
          String whereV=" 6; 5; ";
          et1.setText(whereC);
          et2.setText("Editable Values for Restriction(s):  " );
          et3.setText(whereV);
          cc[1][1]="11"; cc[1][2]="12";
          cc1=cc; 
          Log.d(" cc=cc1", "cc[1][1]=  " + cc1[1][1] + "cc[1][2]=  " + cc1[1][2]);
     }
      public void createDB(View v)
   {   
    	dbHelper = new DBHelper(this, DBName);	
	   db = dbHelper.getWritableDatabase();
	   Log.d("Create DB=","The DB " +DBName+ "  was created OR Opened the exiting one!");
	    }
    public void createTAB(View v)
    {
    	String aDataRow = "";
		String aBuffer = "";
		String tt;
		int nf;
		String [] fieldsN=new String[10];
		String [] fieldsT=new String[10];
		tabel=new FileOper();
		String tabN="tables3";
		Log.d("Read tabN=",tabN);
		aBuffer=tabel.readTable(tabN);
  		tt=aBuffer;
		aBuffer="";
    	String [] tn=tt.toString().split("\n");
    	int nt=tn.length;
    	numberOfTables=nt;
    	Log.d("","N="+String.valueOf(nt)+"  Nume tabel="+tn[0]+",  Nume tabel2="+tn[1]);
    	for(int i=0;i<nt;i++) tabNames[i]=tn[i];
    	for(int i=0;i<nt;i++)
    	{
    		boolean te=exists(tn[i]);
    		if(!te)
    		{
    		tabStruct=tabel.readTable(tn[i]+"s");
    		tabContent=tabel.readTable(tn[i]);
    		String [] tfS=tabStruct.split("\n");// structura
    		String [] tfC=tabContent.split("\n");//  Content
    		nf=tfS.length;
    		 for(int j=0;j<nf;j++)
    		  {
    			  String [] fields=tfS[j].split("\t");
    			  fieldsN[j]=fields[0];
    			  fieldsT[j]=fields[1];
    		  }
    	Log.d("Tabelul : ",tn[i]);
    		 Log.d("Fields",fieldsN[0] + " , "+ fieldsT[0]+" , "+fieldsN[1]+ " , "+fieldsT[1]);
    	
    	// create table i
    		
    		 boolean  tableExists=false;
    	
    		 Log.d("before try Table:", tn[i]+ "   to create???   ");
    	    	try
    	    	{
    	    	// creating a table
    	    		dbHelper.createT(db, tn[i], fieldsN, fieldsT, nf);
    	    	 tableExists = true;
    	    	
    	    	Log.d("Table:", "The  "+ tn[i] + "   was created   ");
    	    	}
    	    	catch (Exception e) {
    	    	   // /* fail */
    	    		Log.d("Table:", "The table "+ tn[i] + "  was existing, and was not created again   ");
    	    	}
    	}
    	
    	}
        
  
    }
    public boolean exists(String table) {
    	Cursor c = null;
    	boolean tableExists = false;
    	/* get cursor on it */
    	try
    	{
    	    c = db.query(table, null,
    	        null, null, null, null, null);
    	        tableExists = true;
    	        Log.d("About existing ", "The table "+table+"  exists! :))))");
    	}
    	catch (Exception e) {
    	    /* fail */
    	    Log.d("The table is missing", table+" doesn't exist :(((");
    	
    	}

    	return tableExists;
	}
    
    public void fillTAB(View v)
    {
    	String tabN;
    	for(int i=0;i<numberOfTables;i++)
    	{
    		tabN=tabNames[i];
    		boolean te=exists(tabN);
    	
    	 Log.d("Before if", tabN);
    if(te) 
    {  //if the tb exists then fill it
    	 Log.d("Inside  if", "The table:  "+tabN+ "   Exists");
    //the table exests:  	//clear the table
    	db.delete(tabN,null,null);
    	 Log.d("After delete",tabN);
    //fill the table
    	String tt, tabContent;
		int nf;
		tabel=new FileOper();
		tabContent=tabel.readTable(tabN);
		Log.d("After table content",tabN);
		String [] tfC=tabContent.split("\n");//  Content
    	int nr=tfC.length;
    	String [] fieldsN =tfC[0].split("\t");
    	String [] fieldsT =tfC[1].split("\t");
    	nf=fieldsN.length;
		Log.d("FIELDS", "FLD:  "+tfC[0]);
		Log.d("TIPES", "TIPES:  "+tfC[1]);
    	 // insert rows
    	ContentValues cv = new ContentValues();
    	int sw;double nnf;
    	for (int j=2;j<nr;j++) //on rows nr=tfC.length;
    {
    		 cv.clear();
       	      if (tabN.equals("detbord")) 
       	      { String [] rcd=tfC[j].toString().split("\t");   
       	    	nnf =Float.valueOf(rcd[2])*0.15  + Float.valueOf(rcd[3])*0.15
       	    			+ Float.valueOf(rcd[4])*0.1+Float.valueOf(rcd[5])*0.2
       	    			+Float.valueOf(rcd[6])*0.4;
       	         	tfC[j]=tfC[j]+"\t"+String.valueOf(df.format(nnf));
       	      }
       	   String cvv="";
       	      String [] rcd=tfC[j].toString().split("\t");
       	          for (int k=0;k<nf;k++)// on fields nf=fieldsN.length;
       	        {
       	        	sw=Integer.valueOf(fieldsT[k]);
       	        	switch (sw)
       	        	{
       	        	case 1:
       	        	//cvv+=	fieldsN[k]+"  "+Integer.valueOf(rcd[k].toString()+", ");
       	        	cv.put(fieldsN[k], Integer.valueOf(rcd[k].toString()));
       	        	  	break;
       	        	case 2:
						//cvv+=	fieldsN[k]+"  "+rcd[k].toString()+", ";
           	        	cv.put(fieldsN[k], rcd[k].toString());
           	        	break;
       	        	default:
           	        	break;
       	        	}
       	         }// end of fields
       	      db.insert(tabN, null, cv);
       	   
    
    }  // end of rows
   // show the table content
    	 Log.d("Datele in tabel", "------" +tabN);
  	   Cursor cc=null;
  	    cc = db.query(tabN, null, null, null, null, null, null);
		int n=cc.getCount();
		tv.setText("\nNR="+n+"\n");
  	    logCursor(cc);
  	    cc.close();
  	    Log.d("Datele in tabel","--- ---");
       }// end of  if(te) (if the tb exists then fill it
    }//  end of for(int i=0;i<numberOfTables;i++)
    
    }
    public void studL(View v)
    {
    	dbHelper = new DBHelper(this, DBName);	
 	   db = dbHelper.getWritableDatabase();
 	    String  whereC=et1.getText().toString().trim();
       String  whereV=et3.getText().toString().trim(); 
       whereV=whereV.replace(" ", "");
       String [] s2=whereV.split(";");
        int np=s2.length;
        //whereC=s1[0];
         for (int j=0;j<np;j++)
        	s2[j]=s2[j].trim();
         String SalesInfo;
		SalesInfo= "select PR.prodid as prID, PR.prodn AS prN,PR.price AS PPrice, SM.quants as SQuant "
                + "from Product as PR "
                + "inner join Sale3 as SM "
                + "on SM.idprod=PR.prodid "
                + "where " +whereC;
         Cursor c;
        c=null;
        c = db.rawQuery(SalesInfo, s2);
          logCursor1(c);
       c.close();
       txtData.setText("Example of Restrictions: "+"s2[0]="+s2[0]+", s2[1]="+s2[1]+","+whereC );

	}
     // afisare in LOG din Cursor
    void logCursor(Cursor c) {
     tv.setText("");
		String str2="Results For the Fields : ";String cnn="",coln="";
    	if (c != null) {
        if (c.moveToFirst()) {
          String str,str1;
         int klu=0,rr=0; 
          do {
        	  rr=0;  str = "";str1="";
            if (klu==0) //  the first record
			  {
				  for (String cn : c.getColumnNames())
				  {
					  {  str = str.concat( c.getString(c.getColumnIndex(cn)) + "; ");
						  cnn=cnn.concat(cn+" ; ");
						  if(rr==2) cnn=cnn+"\n";
					  }
				  rr++;
				  }
			  }
           // for the next records 
            rr=0;  str = "";str1="";
            if (klu>0) //  the next records
            {  
            	for (String cn : c.getColumnNames())
            	  { if(rr>1)
            	       {//str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
                    str = str.concat( c.getString(c.getColumnIndex(cn)) + "; ");
                     cnn=cnn.concat(cn+" ; ");
               	       }
            	   rr++;
                 }   
             }
              if (klu==0) {str2=str2 + cnn;tv.setText(cnn+"\n");}
            klu++;   rr++;
            str1=str+"\n";
            Log.d(" RINDUL Cursr=", str);
            tv.setText(tv.getText()+str1);
           } while (c.moveToNext());
         }
       } else  Log.d("Rindul", "Cursor is null");
    }
    // afisare in LOG din Cursor
    void logCursor1(Cursor c) {
    Log.d("COLUMNS NR=","nc="+c.getColumnCount());
        	tv.setText(""); String str2="Results For the Fields : ";String cnn="",coln="";
    	if (c != null) {
        if (c.moveToFirst()) {
          String str,str1;
            int klu=0,rr=0;
          do {
        	  rr=0; str = "";str1="";
            // for the next records
        	  	for (String cn : c.getColumnNames())
            	  	{if(rr>-1)
            	       {
                    str = str.concat( c.getString(c.getColumnIndex(cn)) + "; ");
                     cnn=cnn.concat(cn+" ; ");
            	       }
            	       rr++;
            	       Log.d("COLUMNS NR=","nc="+c.getColumnCount()+", rr="+rr);
                 	}
                  	if (klu==0) {str2=str2 + cnn;tv.setText(cnn+"\n");}
                        klu++;  rr++;
           			 str1=str+"\n";
            		tv.setText(tv.getText()+str1);
              } while (c.moveToNext());
             }
             } else  Log.d("Rindul", "Cursor is null");
	}

  @Override
	public void onClick(View v) {
		Intent intent = new Intent(this, Intent2.class);
		startActivityForResult(intent, 111);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {return;}
		String name = data.getStringExtra("name");
		tv.setText("The entered name:    " + name);

	}

}
