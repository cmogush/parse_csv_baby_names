//created by Christopher Mogush

import edu.duke.*;
import org.apache.commons.csv.*;
import java.util.Scanner;
import java.io.*;

public class BabyBirths {
    public void printNames(){
        FileResource fr = new FileResource();
        for (CSVRecord rec : fr.getCSVParser(false)){ //false means the CSV does not have headers
            int numBorn = Integer.parseInt(rec.get(2));
            if (numBorn <= 100){
                System.out.println("Name " + rec.get(0) + "Gender " + rec.get(1) + "Num Born " + rec.get(2));
            }
        }
    }
    
    public void totalBirths (FileResource fr) {
        int totalBirths = 0;
        int totalBoys = 0;
        int totalGirls = 0;
        int totalNames = 0;
        int totalGirlNames = 0;
        int totalBoyNames = 0;
        for (CSVRecord rec : fr.getCSVParser(false)) {
            int numBorn = Integer.parseInt(rec.get(2));
            totalBirths += numBorn;
            if (rec.get(1).equals("M")){
                totalBoys += numBorn;
                totalBoyNames += 1;
            }
            if (rec.get(1).equals("F")){
                totalGirls += numBorn;
                totalGirlNames += 1;
            }
            totalNames += 1;
        }
        System.out.println("Total births: " + totalBirths);
        System.out.println("Total boys: " + totalBoys);
        System.out.println("Total girls: " + totalGirls);
        System.out.println("Total names: " + totalNames);
        System.out.println("Total boy names: " + totalBoyNames);
        System.out.println("Total girl names: " + totalGirlNames);
    }
    
    public void testTotalBirths () {
        FileResource fr = new FileResource();
        totalBirths(fr);
    }
    
    public int getRank(FileResource frYear, String name, String gender){
        int rank = -1; //set to -1 for default
        int currRank = 0; //# to subtract from the totalRankCount to find actual rank
        for (CSVRecord rec : frYear.getCSVParser(false)) {
            if(rec.get(1).equals(gender)){
                currRank += 1;
                if(rec.get(0).equals(name)){
                    rank += currRank + 1; //+1 to cancel out -1
                }
            }
        }
        //returns the rank of the name in the file for the given gender (1 being highest) and -1 if no name
        return rank;
    }
    
    public void testGetRank(){
        FileResource frYear = new FileResource();
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter name to check: ");
        String name = scan.next();
        System.out.println("Enter gender (M/F): ");
        String gender = scan.next();
        System.out.println("Enter year to check: ");
        int year = scan.nextInt();
        int rank = getRank(frYear, name, gender);
        System.out.println("The name " + name + " was ranked " + rank + " for the year " + year);
    }
    
    public String getName(FileResource frYear, int rank, String gender){
        int currRank = 0; //# to subtract from the totalRankCount to find actual rank
        String name = "NO NAME";
        for (CSVRecord rec : frYear.getCSVParser(false)) {
            if(rec.get(1).equals(gender)){
                currRank += 1;
                if(currRank == rank){
                    name = rec.get(0); //+1 to cancel out -1
                }
            }
        }
        //returns the rank of the name in the file for the given gender (1 being highest) and -1 if no name
        //returns name at a given rank, for the given gender (rank 1 is highest), if not ranked "NO NAME" is returned
        return name;
    }
    
    public void testGetName(){
        FileResource frYear = new FileResource();
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter a rank to check: ");
        int rank = scan.nextInt();
        System.out.println("Enter gender (M/F): ");
        String gender = scan.next();
        System.out.println("Enter the year of the file: ");
        int year = scan.nextInt();
       String name = getName(frYear, rank, gender);
        System.out.println("The name ranked " + rank + " for the year " + year + " is: " + name);
    }
    
    public String whatNameInYear(String name, FileResource frYear, FileResource frNewYear, String gender){
        String newName = "NO NAME";
        int rank = getRank(frYear, name, gender);
        newName = getName(frNewYear, rank, gender);
        //determines what name would have been named in a diff year, based on rank and gender
        return newName;
    }
    
    public void testWhatNameInYear(){
        FileResource frYear = new FileResource();
        FileResource frNewYear = new FileResource();
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter a name to check: ");
        String name = scan.next();
        System.out.println("Enter gender (M/F): ");
        String gender = scan.next();
        System.out.println("Enter the year of the file where the name appears: ");
        int year = scan.nextInt();
        System.out.println("Enter the year of the file to compare names at the given rank: ");
        int newYear = scan.nextInt();
        String newName = whatNameInYear(name, frYear, frNewYear, gender);
        System.out.println(name + " born in " + year + " would be " + newName + " if born in " + newYear);
    }
    
    public int yearOfHighestRank(String name, String gender){
        //method which selects range of files to process
        int highestRank = 9999999;
        String lowestFile = "";
        DirectoryResource dr = new DirectoryResource(); //iterates over many files
        for (File f: dr.selectedFiles()){
            FileResource frYear = new FileResource(f);
            int rank = getRank(frYear, name, gender);
            if (rank != -1){
                if (rank < highestRank){
                     lowestFile =  f.getName();
                     highestRank = rank;
                }
            }
        }
        //returns an int, the year with the highest rank for name and gender given
        //if no name or gender are found in selected files, it should return -1
        int year = convertFileNameToYear(lowestFile);
        return year;
    }
    
    public void testYearOfHighestRank(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter a name to check: ");
        String name = scan.next();
        System.out.println("Enter gender (M/F): ");
        String gender = scan.next();
        int year = yearOfHighestRank(name, gender);
        System.out.println(year + " is the year the name " + name + " was most popular");
    }
    
    public double getAverageRank(String name, String gender){
        //method selects range of files
        double sumRank = 0;
        double avgRank = 0;
        double countFiles = 0;
        DirectoryResource dr = new DirectoryResource(); //iterates over many files
        for (File f: dr.selectedFiles()){
            FileResource frYear = new FileResource(f);
            int rank = getRank(frYear, name, gender);
            if (rank != -1){
            sumRank += rank;
            }
            countFiles += 1;
        }
        if (sumRank == 0){
            avgRank = -1;
        }
        else{
            avgRank = sumRank / countFiles;
        }
        //returns double representing avg rank of name and gender over selected files 
        //(return -1 if name is not ranked in ANY of selected files)
        System.out.println("sumRank: " + sumRank + " total # of files: " + countFiles + " Average Rank: " + avgRank);
        return avgRank;
    }
    
    public void testGetAverageRank(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter a name to check: ");
        String name = scan.next();
        System.out.println("Enter gender (M/F): ");
        String gender = scan.next();
        double avgRank = getAverageRank(name, gender);
        System.out.println("The average rank for the name " + name + " is: " + avgRank);
    }
    
    public int getTotalBirthsRankedHigher(FileResource frYear, String name, String gender){
        int totalBirths = 0;
        for (CSVRecord rec : frYear.getCSVParser(false)) {
            if(rec.get(1).equals(gender)){
                if(rec.get(0).equals(name)){
                    break;
                }
                int numBorn = Integer.parseInt(rec.get(2));
                totalBirths += numBorn;
            }
        }
        //returns int, total num of births of those names (with same gender and year) who are ranked higer than "name"
        return totalBirths;
    }
    
    public void testGetTotalBirthsRankedHigher(){
        FileResource frYear = new FileResource();
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter name to check: ");
        String name = scan.next();
        System.out.println("Enter gender (M/F): ");
        String gender = scan.next();
        System.out.println("Enter year being checked: ");
        int year = scan.nextInt();
        int totalBirths = getTotalBirthsRankedHigher(frYear, name, gender);
        System.out.println("For " + year + " there were " + totalBirths + " born with names ranked higher than " + name);
    }
    
    public int convertFileNameToYear(String filename){
        int startIndex = filename.indexOf("b");
        int stopIndex = filename.indexOf(".");
        String year = filename.substring(startIndex + 1, stopIndex);
        int yearInt = Integer.parseInt(year);
        return yearInt;
    }
}