/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment3;

import java.awt.event.ActionListener;
import static java.awt.image.ImageObserver.ERROR;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author kaize
 */
public class hw3 extends javax.swing.JFrame {

    private Connection con = null;
    private Statement smt = null;
    private ResultSet rs = null;
    private ArrayList<JCheckBox> main_categories_box = new ArrayList<>();
    private String[] main_cat = {"Active Life", "Arts & Entertainment", "Automotive", "Car Rental", "Cafes", "Beauty & Spas", "Convenience Stores", "Dentists", "Doctors", "Drugstores", "Department Stores", "Education", "Event Planning & Services", "Flowers & Gifts", "Food", "Health & Medical", "Home Services", "Home & Garden", "Hospitals", "Hotels & Travel", "Hardware Stores", "Grocery", "Medical Centers", "Nurseries & Gardening", "Nightlife", "Restaurants", "Shopping", "Transportation"};
    private ArrayList<String> selected_main_categories = new ArrayList<>();
    private ArrayList<String> returned_sub_categories = new ArrayList<>();
    private ArrayList<JCheckBox> sub_categories_box = new ArrayList<>();
    private HashSet<String> returned_bid_main_categories = new HashSet<>();
    private ArrayList<String> selected_sub_categories = new ArrayList<>();
    private ArrayList<String> returned_bname_submit = new ArrayList<>();
    private ArrayList<String> returned_city_submit = new ArrayList<>();
    private ArrayList<String> returned_state_submit = new ArrayList<>();
    private ArrayList<String> returned_u_name_submit = new ArrayList<>();
    private ArrayList<String> returned_y_since_submit = new ArrayList<>();
    private ArrayList<String> returned_u_stars_submit = new ArrayList<>();
    private ArrayList<String> returned_stars_submit = new ArrayList<>();
    private ArrayList<String> returned_u_id_submit = new ArrayList<>();
    private HashSet<String> returned_user_id = new HashSet<>();
    private int flag = -1;
    private int submit_flag = -1;
    private HashSet<String> returned_user_id_reviews = new HashSet<>();
    private HashSet<String> returned_bid_final = new HashSet<>();
    private String get_sub_category_query = "";
    private String get_bid_sub_category_query = "";
    private String get_checkin_query = "";
    private String query = "";
    String query_reviews = "";
    String query_submit = "";
    String query_users = "";
    String from_day = "";
    String to_day = "";
    int from_hour = 0;
    int to_hour = 0;
    String number_checkin = "";
    int val_checkin = 0;

    public hw3() {
        initComponents();

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:yelp", "scott", "tiger");
            smt = con.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < main_cat.length; i++) {
            main_categories_box.add(new JCheckBox(main_cat[i]));
            categories_panel.add(main_categories_box.get(i));
        }
        Business.setVisible(true);
        categories_panel.setLayout(new BoxLayout(categories_panel, BoxLayout.Y_AXIS));
        categories_panel.setVisible(true);
        pack();

        submit_main_category.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                flag = -1;
                submit_flag = -1;
                selected_main_categories.clear();
                selected_sub_categories.clear();
                sub_categories_box.clear();
                get_bid_sub_category_query = "";
                sub_categories_panel.removeAll();
                sub_categories_panel.repaint();
                returned_sub_categories.clear();
                get_checkin_query = "";
                query = "";
                query_reviews = "";
                query_submit = "";
                query_users = "";
                from_day = "";
                to_day = "";
                from_hour = 0;
                to_hour = 0;
                number_checkin = "";
                get_sub_category_query = "";
                val_checkin = 0;

                for (int i = 0; i < main_categories_box.size(); i++) {
                    if (main_categories_box.get(i).isSelected()) {
                        selected_main_categories.add(main_categories_box.get(i).getText());
                    }
                }
                for (int i = 0; i < selected_main_categories.size(); i++) {
                    get_sub_category_query = get_sub_category_query + "SELECT bid FROM b_main_category WHERE c_name = '" + selected_main_categories.get(i) + "'";
                    if (i != selected_main_categories.size() - 1) {
                        get_sub_category_query = get_sub_category_query + " INTERSECT ";
                    } else {
                        //get_sub_category_query = get_sub_category_query + ")";
                        get_bid_sub_category_query = "SELECT DISTINCT bid FROM b_main_category WHERE bid IN (" + get_sub_category_query + ")";
                        get_sub_category_query = "SELECT DISTINCT c_name FROM b_sub_category WHERE bid IN ( " + get_sub_category_query + ")ORDER BY c_name ASC";
                    }
                }
                display_query.setText("");
                display_query.setText(get_sub_category_query);
                flag = 1;
                submit_flag = 1;
                System.out.println("main query = " + get_sub_category_query);
                System.out.println("sub query = " + get_bid_sub_category_query);
                try {
                    if (con != null) {
                        rs = smt.executeQuery(get_sub_category_query);
                        while (rs.next()) {
                            returned_sub_categories.add(rs.getString("c_name"));
                        }
                        for (int i = 0; i < returned_sub_categories.size(); i++) {
                            sub_categories_box.add(new JCheckBox(returned_sub_categories.get(i)));
                            sub_categories_panel.add(sub_categories_box.get(i));
                        }
                        rs = smt.executeQuery(get_bid_sub_category_query);
                        sub_categories_panel.setLayout(new BoxLayout(sub_categories_panel, BoxLayout.Y_AXIS));
                        sub_categories_panel.setVisible(true);;
                        Business.revalidate();
                        Business.setVisible(true);
                        pack();
                        rs.close();

                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        submit_sub_category.addActionListener(new ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {

                get_checkin_query = "";
                query = "";
                query_reviews = "";
                query_submit = "";
                query_users = "";
                from_day = "";
                to_day = "";
                from_hour = 0;
                to_hour = 0;
                number_checkin = "";
                get_sub_category_query = "";
                val_checkin = 0;
                selected_sub_categories.clear();
                for (int i = 0; i < sub_categories_box.size(); i++) {
                    if (sub_categories_box.get(i).isSelected()) {
                        selected_sub_categories.add(sub_categories_box.get(i).getText());
                    }
                }
                for (int i = 0; i < selected_sub_categories.size(); i++) {
                    get_checkin_query = get_checkin_query + "SELECT  bid FROM b_sub_category WHERE c_name = '" + selected_sub_categories.get(i) + "'";
                    if (i != selected_sub_categories.size() - 1) {
                        get_checkin_query = get_checkin_query + " INTERSECT ";
                    } else {
                        get_checkin_query = "SELECT DISTINCT bid FROM b_sub_category WHERE bid IN ( " + get_checkin_query + ")";
                        get_checkin_query = get_bid_sub_category_query + " INTERSECT " + get_checkin_query;
                    }
                }
                submit_flag = 2;
                display_query.setText("");
                display_query.setText(get_checkin_query);
                try {
                    rs = smt.executeQuery(get_checkin_query);
                    rs.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });

        submit_checkin.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {

                query = "";
                query_reviews = "";
                query_submit = "";
                query_users = "";
                from_day = "";
                to_day = "";
                from_hour = 0;
                to_hour = 0;
                number_checkin = "";
                val_checkin = 0;

                from_day = from_day_checkin.getSelectedItem().toString();
                to_day = to_day_checkin.getSelectedItem().toString();
                from_hour = Integer.parseInt(from_hour_checkin.getSelectedItem().toString());
                to_hour = Integer.parseInt(to_hour_checkin.getSelectedItem().toString());
                String[] days = {"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};
                ArrayList<String> day_list = new ArrayList<String>(Arrays.asList(days));
                String day = from_day;
                if (flag == 1) {
                    query = "SELECT bid, SUM(ci_count) AS count FROM check_in WHERE bid IN ( ";
                    query = query + get_checkin_query + ") AND ( ";
                    if (from_day == to_day) {
                        query = query + "ci_day='" + from_day + "' and ( ci_hour >=" + from_hour + " AND ci_hour <= " + to_hour + "))GROUP BY bid";
                        submit_flag = 3;
                    } else {
                        while (day != to_day) {
                            System.out.println("day " + day);
                            if (day == from_day) {
                                query = query + "(ci_day = '" + from_day + "' AND ci_hour >= " + from_hour + ")";
                            } else {
                                query = query + " OR ci_day = '" + day + "'";
                            }
                            day = day_list.get(day_list.indexOf(day) + 1);
                        }
                        query = query + " OR " + "(ci_day = '" + to_day + "' AND ci_hour <= " + to_hour + "))GROUP BY bid ORDER BY count DESC";
                        submit_flag = 3;
                    }
                    display_query.setText("");
                    display_query.setText(query);
                    String number_checkin = num_checkin.getSelectedItem().toString();
                    int val_checkin = Integer.parseInt(value_checkin.getText().toString());
                    query = "SELECT bid FROM (" + query + ") WHERE count" + number_checkin + val_checkin;
                    System.out.println("query=" + query);
                    try {
                        rs = smt.executeQuery(query);
                        rs.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }

                }

            }
        });

        submit_reviews.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                query_reviews = "";
                query_submit = "";
                query_users = "";

                String from_month = from_month_reviews.getSelectedItem().toString();
                int from_date = Integer.parseInt(from_date_reviews.getSelectedItem().toString());
                int from_year = Integer.parseInt(from_year_reviews.getSelectedItem().toString());
                String to_month = to_month_reviews.getSelectedItem().toString();
                int to_date = Integer.parseInt(to_date_reviews.getSelectedItem().toString());
                int to_year = Integer.parseInt(to_year_reviews.getSelectedItem().toString());
                String num_stars = num_stars_reviews.getSelectedItem().toString();
                String num_votes = num_votes_reviews.getSelectedItem().toString();
                double value_stars = Double.parseDouble(value_stars_reviews.getText().toString());
                //int value_votes = Integer.parseInt(value_votes_reviews.getText().toString());

                if (flag == 1) {

                    query_reviews = "SELECT bid FROM check_in WHERE bid IN (";
                    query_reviews = query_reviews + query + ") INTERSECT";
                    query_reviews = query_reviews + " SELECT bid FROM ( SELECT bid, avg(stars) AS avg_stars FROM reviews WHERE (review_date>= '" + from_date + "-" + from_month.substring(0, 3) + "-" + Integer.toString(from_year).substring(2,4) + "'" + " AND review_date<= '" + to_date + "-" + to_month.substring(0,3) + "-" + Integer.toString(to_year).substring(2,4) + "')";
                    query_reviews = query_reviews + " GROUP BY bid )";
                    query_reviews = query_reviews + " WHERE avg_stars " + num_stars + value_stars;
                    submit_flag = 4;
                    display_query.setText("");
                    display_query.setText(query_reviews);
                }
                try {
                    rs = smt.executeQuery(query_reviews);
                    rs.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {

                returned_bname_submit.clear();
                returned_city_submit.clear();
                returned_state_submit.clear();
                returned_stars_submit.clear();
                returned_u_name_submit.clear();
                returned_y_since_submit.clear();
                returned_u_stars_submit.clear();
                returned_u_id_submit.clear();

                if (flag == 1) {
                    if (submit_flag == 1) {
                        query_submit = "SELECT DISTINCT b_name,city,state,stars FROM BUSINESS WHERE bid IN ( " + get_bid_sub_category_query + ") ORDER BY b_name";
                        display_query.setText("");
                        display_query.setText(query_submit);

                    } else if (submit_flag == 2) {
                        query_submit = "SELECT DISTINCT b_name,city,state,stars FROM BUSINESS WHERE bid IN ( " + get_checkin_query + ") ORDER BY b_name";
                        display_query.setText("");
                        display_query.setText(query_submit);
                    } else if (submit_flag == 3) {
                        query_submit = "SELECT DISTINCT b_name,city,state,stars FROM BUSINESS WHERE bid IN ( " + query + ") ORDER BY b_name";
                        display_query.setText("");
                        display_query.setText(query_submit);
                    } else if (submit_flag == 4) {
                        query_submit = "SELECT DISTINCT b_name,city,state,stars FROM BUSINESS WHERE bid IN ( " + query_reviews + ") ORDER BY b_name";
                        display_query.setText("");
                        display_query.setText(query_submit);
                    }
                    System.out.println("quursss" + query_submit);
                } else if (flag == 0) {
                    query_submit = query_submit + "SELECT DISTINCT user_id,user_name,yelping_since,average_stars FROM yelp_user WHERE user_id IN ( ";
                    query_submit = query_submit + query_users + " ) ORDER BY user_name";
                }

                display_query.setText("");
                display_query.setText(query_submit);
                System.out.println("QUERYYY" + query_submit);
                try {
                    rs = smt.executeQuery(query_submit);
                    
                   
                    while (rs.next()) {
                        if (flag == 1) {
                            String bn = rs.getString("b_name");
                            returned_bname_submit.add(bn);
                            System.out.println(bn);
                            returned_city_submit.add(rs.getString("city"));
                            returned_state_submit.add(rs.getString("state"));
                            returned_stars_submit.add(rs.getString("stars"));

                        } else if (flag == 0) {
                            returned_u_id_submit.add(rs.getString("user_id"));
                            returned_u_name_submit.add(rs.getString("user_name"));
                            returned_y_since_submit.add(rs.getString("yelping_since"));
                            returned_u_stars_submit.add(rs.getString("average_stars"));
                        }

                    }
                    System.out.println(("processed"));
                    DefaultTableModel model = new DefaultTableModel();
                    model = (DefaultTableModel) result_table.getModel();
                    model.setRowCount(0);
                    result_panel.repaint();
                    if (flag == 1) {
                        String[] column_name_business = {"Business Name", "City", "State", "Stars"};
                        model.setColumnIdentifiers(column_name_business);
                        result_table.revalidate();
                        for (int i = 0; i < returned_bname_submit.size(); i++) {
                            model.addRow(new Object[]{returned_bname_submit.get(i), returned_city_submit.get(i), returned_state_submit.get(i), returned_stars_submit.get(i)});
                        }
                        result_panel.repaint();
                    } else if (flag == 0) {
                        String[] column_name_user = {"User_Id", "Name", "Yelping Since", "Stars"};
                        model.setColumnIdentifiers(column_name_user);
                        result_table.revalidate();
                        result_table.repaint();
                        for (int i = 0; i < returned_u_id_submit.size(); i++) {
                            model.addRow(new Object[]{returned_u_id_submit.get(i), returned_u_name_submit.get(i), returned_y_since_submit.get(i), returned_u_stars_submit.get(i)});
                        }
                        result_panel.repaint();
                    }
                    result_table.setVisible(true);
                    result_panel.revalidate();
                    result_panel.repaint();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
        submit_users.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                query = "";
                query_reviews = "";
                query_submit = "";
                query_users = "";
                from_day = "";
                to_day = "";
                from_hour = 0;
                to_hour = 0;
                number_checkin = "";
                val_checkin = 0;
                int member_month = Integer.parseInt(member_since_month.getSelectedItem().toString());
                int member_year = Integer.parseInt(member_since_year.getSelectedItem().toString());
                String num_review = num_review_count.getSelectedItem().toString();
                int value_review = Integer.parseInt(num_review_value.getText().toString());
                String number_of_friends = num_friends.getSelectedItem().toString();
                int value_of_friends = Integer.parseInt(value_friends.getText().toString());
                String num_stars = num_avg_stars.getSelectedItem().toString();
                float value_of_stars = Float.parseFloat(value_stars.getText().toString());
                String andor_select = and_or_select.getSelectedItem().toString();

                if (andor_select == "AND") {
                    query_users = query_users + "SELECT y.user_id FROM yelp_user y,(SELECT COUNT(f.friend_id) AS num_f, y.user_id AS user_id FROM friends f, yelp_user y WHERE y.user_id = f.user_id GROUP BY y.user_id) f WHERE TO_DATE(y.yelping_since,'yyyy-MM') > TO_DATE(' "+ member_month +  "-" +member_year + "','MM-yyyy') ";
                    query_users = query_users + " AND y.review_count" + num_review + value_review + " AND (y.user_id = f.user_id AND f.num_f " + number_of_friends + value_of_friends + ") AND y.average_stars " + num_stars + value_of_stars;
                    display_query.setText("");
                    display_query.setText(query_users);
                } else if (andor_select == "OR") {
                    query_users = query_users + "SELECT y.user_id FROM yelp_user y,(SELECT COUNT(f.friend_id) AS num_f, y.user_id AS user_id FROM friends f, yelp_user y WHERE y.user_id = f.user_id GROUP BY y.user_id) f WHERE y.user_id = f.user_id AND (y.yelping_since >'" + member_year + "-" + member_month + "'";
                    query_users = query_users + " OR y.review_count" + num_review + value_review + " OR f.num_f " + number_of_friends + value_of_friends + " OR y.average_stars " + num_stars + value_of_stars + ")";
                    display_query.setText("");
                    display_query.setText(query_submit);
                    display_query.setText("");
                    display_query.setText(query_users);
                }

                flag = 0;
                System.out.println("kfkjlkfj  " + query_users);
                try {
                    rs = smt.executeQuery(query_users);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }

            }
        });

        close_app.addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                try {

                    smt.close();
                    con.close();
                    System.out.println("Connection closed and exited successfully");
                } catch (Exception errr) {
                    errr.printStackTrace();
                }
                System.exit(0);
            }
        });

        submit_table.addActionListener(new ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                try {
                    DefaultTableModel model1 = new DefaultTableModel();
                    model1 = (DefaultTableModel) business_review_table.getModel();
                    model1.setRowCount(0);

                    DefaultTableModel model2 = new DefaultTableModel();
                    model2 = (DefaultTableModel) result_table.getModel();

                    int selected_row = result_table.getSelectedRow();
                    String b_name_table = model2.getValueAt(selected_row, 0).toString();

                    String get_final_reviews = "";
                    get_final_reviews = "SELECT r_text FROM reviews where bid IN (SELECT bid from business where b_name = '" + b_name_table + "')";
                    rs = smt.executeQuery(get_final_reviews);
                    while (rs.next()) {
                        String review = rs.getString("r_text");
                        model1.addRow(new Object[]{review});

                    }
                    business_review_table.setVisible(true);
                    business_review_table.validate();
                    business_review_table.repaint();
                    business_review_table.repaint();

                } catch (SQLException ex) {
                    Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {

                } catch (Exception err) {
                    err.printStackTrace();
                }
            }
        });

        submit_table_users.addActionListener(new ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                try {
                    DefaultTableModel model1 = new DefaultTableModel();
                    model1 = (DefaultTableModel) business_review_table.getModel();
                    model1.setRowCount(0);

                    DefaultTableModel model2 = new DefaultTableModel();
                    model2 = (DefaultTableModel) result_table.getModel();

                    int selected_row = result_table.getSelectedRow();
                    String b_name_table = model2.getValueAt(selected_row, 0).toString();

                    String get_final_reviews = "";
                    get_final_reviews = "SELECT r_text FROM reviews where user_id IN (SELECT user_id from yelp_user where user_id = '" + b_name_table + "')";
                    System.out.println("qqqqq" + get_final_reviews);
                    rs = smt.executeQuery(get_final_reviews);
                    while (rs.next()) {
                        String review = rs.getString("r_text");

                        model1.addRow(new Object[]{review});
                    }
                    business_review_table.revalidate();
                    business_review_table.repaint();
                    business_review_table.setVisible(true);

                } catch (SQLException ex) {
                    Logger.getLogger(hw3.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popupMenu1 = new java.awt.PopupMenu();
        Business = new javax.swing.JPanel();
        Categories = new javax.swing.JScrollPane();
        categories_panel = new javax.swing.JPanel();
        Sub_Categories = new javax.swing.JScrollPane();
        sub_categories_panel = new javax.swing.JPanel();
        Business_Label = new javax.swing.JLabel();
        submit_main_category = new javax.swing.JButton();
        submit_sub_category = new javax.swing.JButton();
        Checking = new javax.swing.JScrollPane();
        checkin_panel = new javax.swing.JPanel();
        from_day_checkin = new javax.swing.JComboBox<>();
        to_day_checkin = new javax.swing.JComboBox<>();
        from_hour_checkin = new javax.swing.JComboBox<>();
        to_hour_checkin = new javax.swing.JComboBox<>();
        num_checkin = new javax.swing.JComboBox<>();
        value_checkin = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        submit_checkin = new javax.swing.JButton();
        Review = new javax.swing.JScrollPane();
        review_panel = new javax.swing.JPanel();
        from_month_reviews = new javax.swing.JComboBox<>();
        from_date_reviews = new javax.swing.JComboBox<>();
        from_year_reviews = new javax.swing.JComboBox<>();
        to_month_reviews = new javax.swing.JComboBox<>();
        to_date_reviews = new javax.swing.JComboBox<>();
        to_year_reviews = new javax.swing.JComboBox<>();
        num_stars_reviews = new javax.swing.JComboBox<>();
        num_votes_reviews = new javax.swing.JComboBox<>();
        value_stars_reviews = new javax.swing.JTextField();
        value_votes_reviews = new javax.swing.JTextField();
        submit_reviews = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        Result = new javax.swing.JScrollPane();
        result_panel = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        result_table = new javax.swing.JTable();
        submit_table = new javax.swing.JButton();
        submit_table_users = new javax.swing.JButton();
        Users = new javax.swing.JScrollPane();
        users_panel = new javax.swing.JPanel();
        num_review_count = new javax.swing.JComboBox<>();
        num_friends = new javax.swing.JComboBox<>();
        num_avg_stars = new javax.swing.JComboBox<>();
        num_review_value = new javax.swing.JTextField();
        value_friends = new javax.swing.JTextField();
        value_stars = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        member_since_month = new javax.swing.JComboBox<>();
        member_since_year = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        submit_users = new javax.swing.JButton();
        and_or_select = new javax.swing.JComboBox<>();
        submit = new javax.swing.JButton();
        business_review_panel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        business_review_table = new javax.swing.JTable();
        jLabel23 = new javax.swing.JLabel();
        close_app = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        display_query = new javax.swing.JTextArea();

        popupMenu1.setLabel("popupMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Business.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout categories_panelLayout = new javax.swing.GroupLayout(categories_panel);
        categories_panel.setLayout(categories_panelLayout);
        categories_panelLayout.setHorizontalGroup(
            categories_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 232, Short.MAX_VALUE)
        );
        categories_panelLayout.setVerticalGroup(
            categories_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 398, Short.MAX_VALUE)
        );

        Categories.setViewportView(categories_panel);

        Business.add(Categories, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 234, 390));

        javax.swing.GroupLayout sub_categories_panelLayout = new javax.swing.GroupLayout(sub_categories_panel);
        sub_categories_panel.setLayout(sub_categories_panelLayout);
        sub_categories_panelLayout.setHorizontalGroup(
            sub_categories_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 215, Short.MAX_VALUE)
        );
        sub_categories_panelLayout.setVerticalGroup(
            sub_categories_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 388, Short.MAX_VALUE)
        );

        Sub_Categories.setViewportView(sub_categories_panel);

        Business.add(Sub_Categories, new org.netbeans.lib.awtextra.AbsoluteConstraints(241, 34, 217, 380));

        Business_Label.setText("BUSINESS");
        Business.add(Business_Label, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 0, -1, -1));

        submit_main_category.setText("Submit");
        Business.add(submit_main_category, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 430, -1, -1));

        submit_sub_category.setText("Submit");
        Business.add(submit_sub_category, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 430, -1, -1));

        from_day_checkin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Day", "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY" }));

        to_day_checkin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Day", "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY" }));

        from_hour_checkin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hour", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24" }));

        to_hour_checkin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hour", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24" }));

        num_checkin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=", ">", "<", ">=", "<=" }));

        value_checkin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                value_checkinActionPerformed(evt);
            }
        });

        jLabel1.setText("CHECKIN");

        jLabel2.setText("From");

        jLabel3.setText("To");

        jLabel4.setText("Number of Checkins");

        jLabel5.setText("Value");

        submit_checkin.setText("Submit");

        javax.swing.GroupLayout checkin_panelLayout = new javax.swing.GroupLayout(checkin_panel);
        checkin_panel.setLayout(checkin_panelLayout);
        checkin_panelLayout.setHorizontalGroup(
            checkin_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(checkin_panelLayout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(checkin_panelLayout.createSequentialGroup()
                .addGroup(checkin_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(checkin_panelLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(checkin_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addGroup(checkin_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(checkin_panelLayout.createSequentialGroup()
                                    .addComponent(to_day_checkin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(to_hour_checkin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(checkin_panelLayout.createSequentialGroup()
                                    .addGroup(checkin_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel1)
                                        .addComponent(from_day_checkin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(from_hour_checkin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(checkin_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(checkin_panelLayout.createSequentialGroup()
                            .addGap(61, 61, 61)
                            .addComponent(submit_checkin))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, checkin_panelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel5)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(value_checkin, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, checkin_panelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(num_checkin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61))
        );
        checkin_panelLayout.setVerticalGroup(
            checkin_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(checkin_panelLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(45, 45, 45)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(checkin_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(from_day_checkin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(from_hour_checkin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(checkin_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(to_day_checkin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(to_hour_checkin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(num_checkin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(checkin_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(value_checkin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 144, Short.MAX_VALUE)
                .addComponent(submit_checkin)
                .addGap(33, 33, 33))
        );

        Checking.setViewportView(checkin_panel);

        from_month_reviews.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        from_month_reviews.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Month", "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER" }));
        from_month_reviews.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                from_month_reviewsActionPerformed(evt);
            }
        });

        from_date_reviews.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        from_date_reviews.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Date", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));

        from_year_reviews.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        from_year_reviews.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Year", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018" }));

        to_month_reviews.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        to_month_reviews.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Month", "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER" }));
        to_month_reviews.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                to_month_reviewsActionPerformed(evt);
            }
        });

        to_date_reviews.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        to_date_reviews.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Date", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));
        to_date_reviews.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                to_date_reviewsActionPerformed(evt);
            }
        });

        to_year_reviews.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        to_year_reviews.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Year", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018" }));

        num_stars_reviews.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=", ">", "<", ">=", "<=" }));

        num_votes_reviews.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=", ">", "<", ">=", "<=" }));

        submit_reviews.setText("Submit");
        submit_reviews.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submit_reviewsActionPerformed(evt);
            }
        });

        jLabel6.setText("From");

        jLabel7.setText("To");

        jLabel8.setText("Number of Stars");

        jLabel9.setText("Value");

        jLabel10.setText("VOTES");

        jLabel11.setText("Value");

        jLabel12.setText("REVIEWS");

        javax.swing.GroupLayout review_panelLayout = new javax.swing.GroupLayout(review_panel);
        review_panel.setLayout(review_panelLayout);
        review_panelLayout.setHorizontalGroup(
            review_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(review_panelLayout.createSequentialGroup()
                .addGroup(review_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(review_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(review_panelLayout.createSequentialGroup()
                            .addGap(41, 41, 41)
                            .addGroup(review_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(review_panelLayout.createSequentialGroup()
                                    .addGap(10, 10, 10)
                                    .addComponent(to_month_reviews, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel7)))
                        .addGroup(review_panelLayout.createSequentialGroup()
                            .addGap(58, 58, 58)
                            .addComponent(jLabel10)))
                    .addGroup(review_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(review_panelLayout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(from_month_reviews, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, review_panelLayout.createSequentialGroup()
                            .addComponent(jLabel6)
                            .addGap(66, 66, 66))))
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(review_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(to_date_reviews, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(review_panelLayout.createSequentialGroup()
                        .addComponent(from_date_reviews, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addGroup(review_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(from_year_reviews, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(to_year_reviews, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(62, Short.MAX_VALUE))
            .addGroup(review_panelLayout.createSequentialGroup()
                .addGroup(review_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(review_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(review_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(review_panelLayout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(jLabel8))
                            .addGroup(review_panelLayout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addComponent(value_stars_reviews, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(review_panelLayout.createSequentialGroup()
                                .addGap(107, 107, 107)
                                .addComponent(num_stars_reviews, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(review_panelLayout.createSequentialGroup()
                                .addGap(96, 96, 96)
                                .addComponent(num_votes_reviews, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(review_panelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(review_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(submit_reviews)
                                .addGroup(review_panelLayout.createSequentialGroup()
                                    .addComponent(jLabel11)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(value_votes_reviews, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(review_panelLayout.createSequentialGroup()
                        .addGap(109, 109, 109)
                        .addComponent(jLabel12)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        review_panelLayout.setVerticalGroup(
            review_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(review_panelLayout.createSequentialGroup()
                .addComponent(jLabel12)
                .addGap(36, 36, 36)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(review_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(from_month_reviews, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(from_date_reviews, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(from_year_reviews, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(review_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(to_month_reviews, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(to_date_reviews, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(to_year_reviews, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(num_stars_reviews, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(review_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(value_stars_reviews, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(30, 30, 30)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(num_votes_reviews, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(review_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(value_votes_reviews, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(18, 18, 18)
                .addComponent(submit_reviews)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        Review.setViewportView(review_panel);

        jLabel13.setText("RESULT");

        result_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(result_table);

        submit_table.setText("click to get Reviews on Business");

        submit_table_users.setText("Click to get Reviews done by Users");
        submit_table_users.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submit_table_usersActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout result_panelLayout = new javax.swing.GroupLayout(result_panel);
        result_panel.setLayout(result_panelLayout);
        result_panelLayout.setHorizontalGroup(
            result_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(result_panelLayout.createSequentialGroup()
                .addGroup(result_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(result_panelLayout.createSequentialGroup()
                        .addGap(175, 175, 175)
                        .addComponent(jLabel13))
                    .addGroup(result_panelLayout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addComponent(submit_table, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(result_panelLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(submit_table_users, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(result_panelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(110, Short.MAX_VALUE))
        );
        result_panelLayout.setVerticalGroup(
            result_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(result_panelLayout.createSequentialGroup()
                .addComponent(jLabel13)
                .addGap(23, 23, 23)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(submit_table, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(submit_table_users, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(391, Short.MAX_VALUE))
        );

        Result.setViewportView(result_panel);

        num_review_count.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=", ">", "<", ">=", "<=" }));

        num_friends.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=", ">", "<", ">=", "<=" }));

        num_avg_stars.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=", ">", "<", ">=", "<=" }));

        jLabel14.setText("USERS");

        jLabel15.setText("Member Since");

        jLabel16.setText("Review Count");

        jLabel17.setText("Number of Friends");

        member_since_month.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        member_since_month.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Month", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));
        member_since_month.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                member_since_monthActionPerformed(evt);
            }
        });

        member_since_year.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        member_since_year.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Year", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018" }));

        jLabel18.setText("Average Stars");

        jLabel19.setText("Select");

        jLabel20.setText("Value");

        jLabel21.setText("Value");

        jLabel22.setText("Value");

        submit_users.setText("Submit");
        submit_users.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submit_usersActionPerformed(evt);
            }
        });

        and_or_select.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AND", "OR" }));

        javax.swing.GroupLayout users_panelLayout = new javax.swing.GroupLayout(users_panel);
        users_panel.setLayout(users_panelLayout);
        users_panelLayout.setHorizontalGroup(
            users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, users_panelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(num_friends, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(num_avg_stars, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(and_or_select, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(num_review_count, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(294, 294, 294))
            .addGroup(users_panelLayout.createSequentialGroup()
                .addGroup(users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(users_panelLayout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(jLabel15)
                        .addGap(41, 41, 41)
                        .addComponent(member_since_month, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addGroup(users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(users_panelLayout.createSequentialGroup()
                                .addGroup(users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(value_stars, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(num_review_value, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(value_friends, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(member_since_year, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(users_panelLayout.createSequentialGroup()
                        .addGap(217, 217, 217)
                        .addComponent(jLabel14))
                    .addGroup(users_panelLayout.createSequentialGroup()
                        .addGap(195, 195, 195)
                        .addComponent(submit_users)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        users_panelLayout.setVerticalGroup(
            users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(users_panelLayout.createSequentialGroup()
                .addComponent(jLabel14)
                .addGroup(users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(users_panelLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(member_since_month, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(member_since_year, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(num_review_count, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(num_review_value, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16)
                            .addComponent(jLabel20))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(value_friends, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(num_friends, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel17)
                                .addComponent(jLabel21)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(value_stars)
                            .addComponent(num_avg_stars, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18)
                            .addComponent(jLabel22))
                        .addGap(37, 37, 37)
                        .addComponent(jLabel19))
                    .addGroup(users_panelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 202, Short.MAX_VALUE)
                        .addComponent(and_or_select, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(submit_users)
                .addGap(255, 255, 255))
        );

        Users.setViewportView(users_panel);

        submit.setText("Submit");
        submit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitActionPerformed(evt);
            }
        });

        business_review_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(business_review_table);

        jLabel23.setText("REVIEW LISTS");

        javax.swing.GroupLayout business_review_panelLayout = new javax.swing.GroupLayout(business_review_panel);
        business_review_panel.setLayout(business_review_panelLayout);
        business_review_panelLayout.setHorizontalGroup(
            business_review_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(business_review_panelLayout.createSequentialGroup()
                .addGroup(business_review_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(business_review_panelLayout.createSequentialGroup()
                        .addGap(156, 156, 156)
                        .addComponent(jLabel23))
                    .addGroup(business_review_panelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        business_review_panelLayout.setVerticalGroup(
            business_review_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(business_review_panelLayout.createSequentialGroup()
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 504, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(54, Short.MAX_VALUE))
        );

        close_app.setText("Close Application");

        display_query.setColumns(20);
        display_query.setLineWrap(true);
        display_query.setRows(5);
        display_query.setWrapStyleWord(true);
        jScrollPane3.setViewportView(display_query);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Business, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Checking, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Review, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Users, javax.swing.GroupLayout.PREFERRED_SIZE, 466, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane3)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(submit, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(197, 197, 197))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(close_app, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(161, 161, 161)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Result, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(business_review_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Business, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Checking)
                    .addComponent(Review))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(submit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(close_app, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15))
                    .addComponent(Users, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(business_review_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(Result, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void submitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_submitActionPerformed

    private void value_checkinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_value_checkinActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_value_checkinActionPerformed

    private void to_date_reviewsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_to_date_reviewsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_to_date_reviewsActionPerformed

    private void from_month_reviewsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_from_month_reviewsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_from_month_reviewsActionPerformed

    private void to_month_reviewsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_to_month_reviewsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_to_month_reviewsActionPerformed

    private void submit_reviewsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submit_reviewsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_submit_reviewsActionPerformed

    private void member_since_monthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_member_since_monthActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_member_since_monthActionPerformed

    private void submit_usersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submit_usersActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_submit_usersActionPerformed

    private void submit_table_usersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submit_table_usersActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_submit_table_usersActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(hw3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(hw3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(hw3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(hw3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new hw3().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Business;
    private javax.swing.JLabel Business_Label;
    private javax.swing.JScrollPane Categories;
    private javax.swing.JScrollPane Checking;
    private javax.swing.JScrollPane Result;
    private javax.swing.JScrollPane Review;
    private javax.swing.JScrollPane Sub_Categories;
    private javax.swing.JScrollPane Users;
    private javax.swing.JComboBox<String> and_or_select;
    private javax.swing.JPanel business_review_panel;
    private javax.swing.JTable business_review_table;
    private javax.swing.JPanel categories_panel;
    private javax.swing.JPanel checkin_panel;
    private javax.swing.JButton close_app;
    private javax.swing.JTextArea display_query;
    private javax.swing.JComboBox<String> from_date_reviews;
    private javax.swing.JComboBox<String> from_day_checkin;
    private javax.swing.JComboBox<String> from_hour_checkin;
    private javax.swing.JComboBox<String> from_month_reviews;
    private javax.swing.JComboBox<String> from_year_reviews;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JComboBox<String> member_since_month;
    private javax.swing.JComboBox<String> member_since_year;
    private javax.swing.JComboBox<String> num_avg_stars;
    private javax.swing.JComboBox<String> num_checkin;
    private javax.swing.JComboBox<String> num_friends;
    private javax.swing.JComboBox<String> num_review_count;
    private javax.swing.JTextField num_review_value;
    private javax.swing.JComboBox<String> num_stars_reviews;
    private javax.swing.JComboBox<String> num_votes_reviews;
    private java.awt.PopupMenu popupMenu1;
    private javax.swing.JPanel result_panel;
    private javax.swing.JTable result_table;
    private javax.swing.JPanel review_panel;
    private javax.swing.JPanel sub_categories_panel;
    private javax.swing.JButton submit;
    private javax.swing.JButton submit_checkin;
    private javax.swing.JButton submit_main_category;
    private javax.swing.JButton submit_reviews;
    private javax.swing.JButton submit_sub_category;
    private javax.swing.JButton submit_table;
    private javax.swing.JButton submit_table_users;
    private javax.swing.JButton submit_users;
    private javax.swing.JComboBox<String> to_date_reviews;
    private javax.swing.JComboBox<String> to_day_checkin;
    private javax.swing.JComboBox<String> to_hour_checkin;
    private javax.swing.JComboBox<String> to_month_reviews;
    private javax.swing.JComboBox<String> to_year_reviews;
    private javax.swing.JPanel users_panel;
    private javax.swing.JTextField value_checkin;
    private javax.swing.JTextField value_friends;
    private javax.swing.JTextField value_stars;
    private javax.swing.JTextField value_stars_reviews;
    private javax.swing.JTextField value_votes_reviews;
    // End of variables declaration//GEN-END:variables
}
