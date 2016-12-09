package com.example.avescera.remindme.Classes;

import android.content.Context;

import com.example.avescera.remindme.DBHandlers.DatabaseContactHandler;
import com.example.avescera.remindme.DBHandlers.DatabaseMoneyHandler;
import com.example.avescera.remindme.DBHandlers.DatabaseObjectHandler;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by a.vescera on 08/12/2016.
 */

public class TestData {

    private InitDataBaseHandlers dbHandlers;

    private SimpleDateFormat dateFormat;

    public TestData(){

    }

    public void testDataPopulation(Context context){
        //dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dbHandlers = new InitDataBaseHandlers(context);

        //Test contacts creation
        Contact testContact1 = new Contact(dbHandlers.getDbContactHandler().getContactsNextId(),"toto1","test1","0102030405","test@email.com");
        dbHandlers.getDbContactHandler().createContact(testContact1);
        Contact testContact2 = new Contact(dbHandlers.getDbContactHandler().getContactsNextId(),"toto2","test2","0102030405","test@email.com");
        dbHandlers.getDbContactHandler().createContact(testContact2);
        Contact testContact3 = new Contact(dbHandlers.getDbContactHandler().getContactsNextId(),"toto3","test3","0102030405","test@email.com");
        dbHandlers.getDbContactHandler().createContact(testContact3);
        Contact testContact4 = new Contact(dbHandlers.getDbContactHandler().getContactsNextId(),"toto4","test4","0102030405","test@email.com");
        dbHandlers.getDbContactHandler().createContact(testContact4);
        Contact testContact5 = new Contact(dbHandlers.getDbContactHandler().getContactsNextId(),"toto5","test5","0102030405","test@email.com");
        dbHandlers.getDbContactHandler().createContact(testContact5);
        Contact testContact6 = new Contact(dbHandlers.getDbContactHandler().getContactsNextId(),"toto6","test6","0102030405","test@email.com");
        dbHandlers.getDbContactHandler().createContact(testContact6);
        Contact testContact7 = new Contact(dbHandlers.getDbContactHandler().getContactsNextId(),"toto7","test7","0102030405","test@email.com");
        dbHandlers.getDbContactHandler().createContact(testContact7);
        Contact testContact8 = new Contact(dbHandlers.getDbContactHandler().getContactsNextId(),"toto8","test8","0102030405","test@email.com");
        dbHandlers.getDbContactHandler().createContact(testContact8);
        Contact testContact9 = new Contact(dbHandlers.getDbContactHandler().getContactsNextId(),"toto9","test9","0102030405","test@email.com");
        dbHandlers.getDbContactHandler().createContact(testContact9);

        //Test Money Creation
        Money testMoney1 = null;
        Money testMoney2 = null;
        Money testMoney3 = null;
        Money testMoney4 = null;
        Money testMoney5 = null;
        Money testMoney6 = null;
        Money testMoney7 = null;
        try {
            testMoney1 = new Money(dbHandlers.getDbMoneyHandler().getMoneysNextId(), "Money test 1", 25, "Details test 1",
                    dateFormat.parse("2016-12-01"), 1, testContact1.get_id(), null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dbHandlers.getDbMoneyHandler().createMoney(testMoney1);
        try {
            testMoney2 = new Money(dbHandlers.getDbMoneyHandler().getMoneysNextId(), "Money test 2", 15, "Details test 2",
                    dateFormat.parse("2016-11-01"), 1, testContact1.get_id(), null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dbHandlers.getDbMoneyHandler().createMoney(testMoney2);
        try {
            testMoney3 = new Money(dbHandlers.getDbMoneyHandler().getMoneysNextId(), "Money test 3", 50, "Details test 3",
                    dateFormat.parse("2016-10-01"), 1, testContact2.get_id(), null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dbHandlers.getDbMoneyHandler().createMoney(testMoney3);
        try {
            testMoney4 = new Money(dbHandlers.getDbMoneyHandler().getMoneysNextId(), "Money test 4", 20, "Details test 4",
                    dateFormat.parse("2016-10-01"), 2, testContact3.get_id(), null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dbHandlers.getDbMoneyHandler().createMoney(testMoney4);
        try {
            testMoney5 = new Money(dbHandlers.getDbMoneyHandler().getMoneysNextId(), "Money test 5", 55, "Details test 5",
                    dateFormat.parse("2016-10-01"), 1, testContact3.get_id(), null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dbHandlers.getDbMoneyHandler().createMoney(testMoney5);
        try {
            testMoney6 = new Money(dbHandlers.getDbMoneyHandler().getMoneysNextId(), "Money test 6", 5, "Details test 6",
                    dateFormat.parse("2016-08-01"), 2, testContact4.get_id(), null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dbHandlers.getDbMoneyHandler().createMoney(testMoney6);
        try {
            testMoney7 = new Money(dbHandlers.getDbMoneyHandler().getMoneysNextId(), "Money test 7", 5, "Details test 7",
                    dateFormat.parse("2016-07-01"), 2, testContact8.get_id(), null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dbHandlers.getDbMoneyHandler().createMoney(testMoney7);

        //Test Object creation
        Object object1 = null;
        Object object2 = null;
        Object object3 = null;
        Object object4 = null;
        Object object5 = null;
        Object object6 = null;
        Object object7 = null;
        Object object8 = null;

        try {
            object1 = new Object(dbHandlers.getDbObjectHandler().getObjectsNextId(), "Object test 1", 1, "Details test 1",
                    dateFormat.parse("2016-12-01"), 3, 2, testContact4.get_id(), null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dbHandlers.getDbObjectHandler().createObject(object1);
        try {
            object2 = new Object(dbHandlers.getDbObjectHandler().getObjectsNextId(), "Object test 2", 4, "Details test 2",
                    dateFormat.parse("2016-11-01"), 4, 2, testContact1.get_id(), null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dbHandlers.getDbObjectHandler().createObject(object2);
        try {
            object3 = new Object(dbHandlers.getDbObjectHandler().getObjectsNextId(), "Object test 3", 1, "Details test 3",
                    dateFormat.parse("2016-09-01"), 3, 2, testContact5.get_id(), null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dbHandlers.getDbObjectHandler().createObject(object3);
        try {
            object4 = new Object(dbHandlers.getDbObjectHandler().getObjectsNextId(), "Object test 4", 2, "Details test 4",
                    dateFormat.parse("2016-09-01"), 5, 2, testContact6.get_id(), null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dbHandlers.getDbObjectHandler().createObject(object4);
        try {
            object5 = new Object(dbHandlers.getDbObjectHandler().getObjectsNextId(), "Object test 5", 1, "Details test 5",
                    dateFormat.parse("2016-07-01"), 5, 1, testContact6.get_id(), null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dbHandlers.getDbObjectHandler().createObject(object5);
        try {
            object6 = new Object(dbHandlers.getDbObjectHandler().getObjectsNextId(), "Object test 6", 3, "Details test 6",
                    dateFormat.parse("2016-06-01"), 5, 1, testContact8.get_id(), null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dbHandlers.getDbObjectHandler().createObject(object6);
        try {
            object7 = new Object(dbHandlers.getDbObjectHandler().getObjectsNextId(), "Object test 7", 1, "Details test 7",
                    dateFormat.parse("2016-06-01"), 3, 2, testContact9.get_id(), null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dbHandlers.getDbObjectHandler().createObject(object7);
        try {
            object8 = new Object(dbHandlers.getDbObjectHandler().getObjectsNextId(), "Object test 8", 2, "Details test 8",
                    dateFormat.parse("2016-06-01"), 3, 1, testContact6.get_id(), null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dbHandlers.getDbObjectHandler().createObject(object8);
    }
}
