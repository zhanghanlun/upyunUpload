package com.zhanghanlun.upyun.Service;

import com.alibaba.excel.EasyExcel;
import com.zhanghanlun.upyun.Entity.Student;
import oracle.jrockit.jfr.jdkevents.throwabletransform.ConstructorTracerWriter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Max;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Service
public class Excel {

    public static void main(String[] args){
        File file = new File("E://zhang.xlsx");
        EasyExcel.read(file);
        try {
            XSSFWorkbook wb =new XSSFWorkbook(file);
            XSSFSheet sheet = wb.getSheet("Student");
//            XSSFRow headRow = sheet.getRow(0);
//            String[] head = new String[headRow.getLastCellNum()];
//            for (int i = 0;i<head.length;i++){
//                head[i] = headRow.getCell(i).toString();
//            }
//
//            Class studentClass = Student.class;
//            Constructor[] cons = null;
//            cons = studentClass.getConstructors();
//            Constructor defCon = cons[0];//得到默认构造器,第0个是默认构造器，无参构造方法
//            List<Student> students = new ArrayList<>();
//            for (int i = 1;i<sheet.getLastRowNum()+1;i++){
//                Object obj = defCon.newInstance();
//                XSSFRow row = sheet.getRow(i);
//                for (int j = 0;j<head.length;j++){
//                    Field field = studentClass.getDeclaredField(head[j]);
//                    field.setAccessible(true);
//                    field.set(obj,row.getCell(j).toString());
//                }
//                students.add((Student) obj);
//            }
//            System.out.println(111);
            List<Student> students = readExcel(Student.class,sheet);
            System.out.println(students);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static <T> List<T> readExcel(Class<T> tClass,XSSFSheet sheet){

        try {
            XSSFRow headRow = sheet.getRow(0);
            String[] head = new String[headRow.getLastCellNum()];
            for (int i = 0;i<head.length;i++){
                head[i] = headRow.getCell(i).toString();
            }
            List<T> list = new ArrayList<>();
            for (int i = 1;i<sheet.getLastRowNum()+1;i++){
                T obj = tClass.newInstance();
                XSSFRow row = sheet.getRow(i);
                for (int j = 0;j<head.length;j++){
                    Field field = tClass.getDeclaredField(head[j]);
                    field.setAccessible(true);
                    field.set(obj,row.getCell(j).toString());
                }
                list.add((T) obj);
            }
            System.out.println(111);
            return list;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getBean(String className) throws Exception {
        Class cls = null;
        try {
            cls = Class.forName(className);//对应Spring ->bean -->class
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new Exception("类错误！");
        }
        Constructor[] cons = null;//得到所有构造器
        try {
            cons = cls.getConstructors();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("构造器错误！");
        }
        if (cons == null || cons.length < 1) {
            throw new Exception("没有默认构造方法！");
        }
        //如果上面没错，就有构造方法

        Constructor defCon = cons[0];//得到默认构造器,第0个是默认构造器，无参构造方法
        Object obj = defCon.newInstance();//实例化，得到一个对象 //Spring - bean -id
        return obj;
    }

}
