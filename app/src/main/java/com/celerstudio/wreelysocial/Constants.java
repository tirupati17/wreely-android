package com.celerstudio.wreelysocial;

import com.liulishuo.filedownloader.util.FileDownloadUtils;

public class Constants {

    public interface PreferenceKeys {
        String USER_LOGGED_IN = "user_logged_in";
        String USER_DATA = "user_data";
        String USER_ID = "user_id";
        String BEAT_INFO = "beat_info";
        String GCM_REGISTRATION_ID = "gcm_registration_id";
        String RETAILER_UPDATE = "retailer_update";
        String ACTIVE_RETAILER = "active_retailer";
        String OUTLET_TYPES = "outlet_types";
        String CART_ITEMS = "cart_items";
        String CHECKIN_ID = "checkin_id";
        String LAT = "lat";
        String LNG = "lng";
        String SAVED_DATE = "saved_date";
        String DATABASE_NAME = "database_name";
    }

    public static CharSequence[] STATES = {"Andaman and Nicobar Islands", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chandigarh", "Chhattisgarh", "Dadra and Nagar Haveli", "Daman and Diu", "Delhi", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Lakshadweep", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Orissa", "Pondicherry", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttaranchal", "West Bengal"};
    public static String[] STATES1 = {"Andaman and Nicobar Islands", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chandigarh", "Chhattisgarh", "Dadra and Nagar Haveli", "Daman and Diu", "Delhi", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Lakshadweep", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Orissa", "Pondicherry", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttaranchal", "West Bengal"};
    public static CharSequence[] ACTIVITY_LEVEL = {"Sedentary (little to no exercise)", "Light exercise (1-3 days per week)", "Moderate exercise (3–5 days per week)", "Heavy exercise (6–7 days per week)", "Very heavy exercise (twice per day, extra heavy workouts)"};
    public static String[] CALCULATORS = {"Framingham cardiac risk score", "Basal Energy Expenditure", "BMI & Body surface area", "HbA1c to eAG", "Creatinine Clearance", "HOMA score"};
    public static String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    public static String[] INSTITUTES = {"Andhra Medical College, Visakhapatnam","Narayana Medical College, Nellore","Sri Venkateswara Institute of Medical Sciences (SVIMS) , Tirupati","Gauhati Medical College, Guwahati","Postgraduate Institute of Medical Education & Research, Chandigarh","All India Institute of Medical Sciences, New Delhi","Sher-I-Kashmir Instt. Of Medical Sciences, Srinagar","M S Ramaiah Medical College, Bangalore","St. Johns Medical College, Bangalore","Vydehi Institute Of Medical Sciences & Research Centre, Bangalore","Amrita School of Medicine, Elamkara, Kochi","Medical College, Thiruvananthapuram","Seth GS Medical College, Mumbai","Topiwala National Medical College, Mumbai","SCB Medical College, Cuttack","Jawaharlal Institute of Postgraduate Medical Education & Research, Puducherry","SMS Medical College, Jaipur","Christian Medical College, Vellore","Gandhi Medical College, Secunderabad","Osmania Medical College, Hyderabad","Institute of Medical Sciences, BHU, Varansi","LLRM Medical College, Meerut","Sanjay Gandhi Postgraduate Institute of Medical Sciences, Lucknow","Institute of Postgraduate Medical Education & Research, Kolkata","Medical College, Kolkata","Nilratan Sircar Medical College, Kolkata","Dr. P.S.I. Medical College , Chinoutpalli","Guntur Medical College, Guntur","Kurnool Medical College, Kurnool","NRI Medical College, Guntur","G.B. Pant Institute of Postgraduate Medical Education and Research, New Delhi","PGIMER Dr. RML Hospital, New Delhi","Vardhman Mahavir Medical College & Safdarjung Hospital, Delhi","B J Medical College, Ahmedabad","SBKS Medical Instt. & Research Centre, Vadodra","Smt. N.H.L.Municipal Medical College, Ahmedabad","Maharishi Markandeshwar Institute Of Medical Sciences & Research, Mullana, Ambala","Indira Gandhi Medical College, Shimla","A J Institute of Medical Sciences & Research Centre, Mangalore","Jawaharlal Nehru Medical College, Belgaum","Kasturba Medical College, Manipal","Rajarajeswari Medical College & Hospital, Bangalore","Sri Jayadeva Institute of Cardiology, Bangalore","S S Institute of Medical Sciences& Research Centre, Davangere","Academy of Medical Sceiences,Pariyaram, Kannur","Amala Institute of Medical Sciences, Thrissur","Government Medical College, Kottayam","Government Medical College, Kozhikode, Calicut","Jubilee Mission Medical College & Research Institute, Thrissur","Pushpagiri Institute Of Medical Sciences and Research Centre, Tiruvalla","Sree Chitra Thirunal Institute for Medical Science and Technology, Thiruvananthapura","T D Medical College, Alleppey (Allappuzha)","Armed Forces Medical College, Pune","Bharati Vidyapeeth University Medical College, Pune","Bombay Hospital Institute of Medical Sciences, Mumbai","Dr. D Y Patil Medical College, Hospital and Research Centre, Pimpri, Pune","Government Medical College, Nagpur","Grant Medical College, Mumbai","Lokmanya Tilak Municipal Medical College, Sion, Mumbai","Mahatma Gandhi Missions Medical College, Aurangabad","Mahatma Gandhi Missions Medical College, Navi Mumbai","Padmashree Dr. D.Y.Patil Medical College, Navi Mumbai","North Eastern Indira Gandhi Regional Instt. of Health and Medical Sciences, Shillong","MKCG Medical College, Berhampur","Mahatma Gandhi Medical College & Research Institute, Pondicherry","Christian Medical College, Ludhiana","Dayanand Medical College & Hospital, Ludhiana","Dr SN Medical College, Jodhpur","Jawaharlal Nehru Medical College, Ajmer","R N T Medical College, Udaipur","Sardar Patel Medical College, Bikaner","Chettinad Hospital & Research Institute, Kanchipuram","Madras Medical College, Chennai","Madurai Medical College, Madurai","Meenakshi Medical College and Research Institute, Enathur","PSG Institute of Medical Sciences, Coimbatore","Saveetha Medical College and Hospital, Kanchipuram","Sri Ramachandra Medical College & Research Institute, Chennai","SRM Medical College Hospital & Research Centre, Kancheepuram","Stanley Medical College, Chennai","Deccan College of Medical Sciences, Hyderabad","Mamata Medical College, Khammam","Nizams Institute of Medical Sciences, Hyderabad","GSVM Medical College, Kanpur","King George Medical University, Lucknow","Burdwan Medical College, Burdwan","RG Kar Medical College, Kolkata"};

    public interface HTTPStatusCodes {
        int OK = 200;
        int EMPTY = 204;
        int UNAUTHORIZED = 401;
        int NOT_FOUND = 404;
    }

    public static double MGDL = 18.0182;

    public interface DB {
        String NAME = "impactguru";
        String TABLE_NOTIFICATION = "notification";
    }

    public static String FOLDER_PATH(String fileName) {
        return FileDownloadUtils.getDefaultSaveRootPath() + "/" + fileName;
    }

    public static final String CHECK_INTERNET_CONNECTION = "Please check internet connection.</br></br><font fgcolor='#0000ff'>Settings</font>.";


}
