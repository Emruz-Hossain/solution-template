package com.tigerit.exam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;


/**
 * All of your application logic should be placed inside this class.
 * Remember we will load your application from our custom container.
 * You may add private method inside this class but, make sure your
 * application's execution points start from inside run method.
 */
public class Solution implements Runnable {
    @Override
    public void run() {
        // your application entry point
        Solution obj = new Solution();
        obj.solve();
    }

    // --------------------------IO Code Begin----------------------------------------
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static String readLine() {
        String value;
        try {
            value = reader.readLine();
            value = value.trim();
        } catch (IOException ex) {
            value = null;
        }
        return value;
    }

    public static void printLine(Object value) {
        System.out.println(value);
    }
    // -------------------------IO Code End------------------------------

    //------------------Main Code Begin-------------------------------
    private static HashMap<String, Integer> tableNameHashValue = new HashMap<String, Integer>();
    private static HashMap<String, Integer> columnNameHashValue = new HashMap<String, Integer>();

    public class Table {
        String tableName;
        Integer numberOfColumns, numberOfRecords;
        String[] columnNames = new String[105];
        Integer[][] Records = new Integer[105][105];
    }

    List<String> resultColumnNames = new ArrayList<String>();
    List<List<Integer>> resultRecords = new ArrayList<List<Integer>>();

    void solve() {
        Integer numberOfTestCases, caseNo, numberOfTable, tableNo, numberOfQuery, queryNo;
        String Line;

        numberOfTestCases = Integer.parseInt(readLine());

        for (caseNo = 1; caseNo <= numberOfTestCases; caseNo++) {
            numberOfTable = Integer.parseInt(readLine());
            Table[] myTables = new Table[numberOfTable + 1];
            for (int i = 1; i <= numberOfTable; i++) {
                myTables[i] = new Table();
            }
            for (tableNo = 1; tableNo <= numberOfTable; tableNo++) {
                myTables[tableNo].tableName = readLine();
                tableNameHashValue.put(myTables[tableNo].tableName, tableNo);

                Line = readLine();
                String[] Temp = Line.split(" ");
                myTables[tableNo].numberOfColumns = Integer.parseInt(Temp[0]);
                myTables[tableNo].numberOfRecords = Integer.parseInt(Temp[1]);

                Line = readLine();
                String[] Temp2 = Line.split(" ");
                for (int i = 1; i <= myTables[tableNo].numberOfColumns; i++) {
                    myTables[tableNo].columnNames[i] = Temp2[i - 1];
                    columnNameHashValue.put(Temp2[i - 1], i);
                }

                for (int recordsNo = 1; recordsNo <= myTables[tableNo].numberOfRecords; recordsNo++) {
                    Line = readLine();
                    String[] Temp3 = Line.split(" ");
                    for (int j = 1; j <= myTables[tableNo].numberOfColumns; j++) {
                        myTables[tableNo].Records[recordsNo][j] = Integer.parseInt(Temp3[j - 1]);
                    }
                }
            }

            System.out.println("Test: " + caseNo);

            numberOfQuery = Integer.parseInt(readLine());
            for (queryNo = 1; queryNo <= numberOfQuery; queryNo++) {
                Line = readLine();
                Line = Line.replaceAll(",", "");
                String[] SELECTLine = Line.split(" ");

                Line = readLine();
                String[] FROMLine = Line.split(" ");

                Line = readLine();
                String[] JOINLine = Line.split(" ");

                Line = readLine();
                String[] ONLine = Line.split(" ");

                if (SELECTLine[1].compareTo("*") == 0) // Either query type 1 or 2
                {

                    Integer firstTableId = tableNameHashValue.get(FROMLine[1]);
                    Integer secondTableId = tableNameHashValue.get(JOINLine[1]);
                    String[] Temp4 = ONLine[1].split("\\.");
                    String[] Temp5 = ONLine[3].split("\\.");
                    Integer firstTableColumnNo = columnNameHashValue.get(Temp4[1]);
                    Integer secondTableColumNo = columnNameHashValue.get(Temp5[1]);

                    // System.out.println(firstTableId+" "+secondTableId+" "+firstTableColumnNo+" "+secondTableColumNo);

                    MatchAndJoinAll(myTables[firstTableId], myTables[secondTableId], firstTableColumnNo,
                            secondTableColumNo);
                } else // query type 3
                {
                    Integer firstTableId = tableNameHashValue.get(FROMLine[1]);
                    Integer secondTableId = tableNameHashValue.get(JOINLine[1]);
                    String[] Temp4 = ONLine[1].split("\\.");
                    String[] Temp5 = ONLine[3].split("\\.");
                    Integer firstTableColumnNo = columnNameHashValue.get(Temp4[1]);
                    Integer secondTableColumNo = columnNameHashValue.get(Temp5[1]);

                    List<Integer> selectedColumns = new ArrayList<Integer>();
                    for (int i = 1; i < SELECTLine.length; i++) {
                        String[] Temp6 = SELECTLine[i].split("\\.");
                        if (Temp6[0].compareTo(Temp4[0]) == 0) // short name of first table
                        {
                            selectedColumns.add(columnNameHashValue.get(Temp6[1]));
                        } else // short name of second table
                        {
                            selectedColumns.add(-1 * columnNameHashValue.get(Temp6[1]));
                        }
                    }

                    MatchAndJoinSome(myTables[firstTableId], myTables[secondTableId], firstTableColumnNo,
                            secondTableColumNo, selectedColumns);
                }
                //------------------Sort result lexicographically--------------------
                int len = resultColumnNames.size();
                Collections.sort(resultRecords, new Comparator<List<Integer>>() {

                    @Override
                    public int compare(List<Integer> A, List<Integer> B) {
                        for (int i = 0; i < A.size(); i++) {
                            int flag = A.get(i).compareTo(B.get(i));
                            if (flag != 0)
                                return flag;
                        }
                        return 0;
                    }

                });

                //---------------------Print Result for this query--------------------
                for (int i = 0; i < resultColumnNames.size(); i++) {
                    if (i > 0)
                        System.out.print(" ");
                    System.out.print(resultColumnNames.get(i));
                }
                System.out.println();

                for (int i = 0; i < resultRecords.size(); i++) {
                    List<Integer> result = resultRecords.get(i);
                    for (int j = 0; j < result.size(); j++) {
                        if (j > 0)
                            System.out.print(" ");
                        System.out.print(result.get(j));
                    }
                    System.out.println();
                }
                System.out.println();
                Line = readLine();
            }

        }
    }

    public void MatchAndJoinAll(Table firstTable, Table secondTable, Integer firstTableColumnNo,
            Integer secondTableColumnNo) {
        Integer i, j, k;
        resultColumnNames = new ArrayList<String>();
        resultRecords = new ArrayList<List<Integer>>();
        for (i = 1; i <= firstTable.numberOfColumns; i++)
            resultColumnNames.add(firstTable.columnNames[i]);
        for (i = 1; i <= secondTable.numberOfColumns; i++)
            resultColumnNames.add(secondTable.columnNames[i]);

        for (i = 1; i <= firstTable.numberOfRecords; i++) {
            for (j = 1; j <= secondTable.numberOfRecords; j++) {
                if (firstTable.Records[i][firstTableColumnNo] == secondTable.Records[j][secondTableColumnNo]) {
                    List<Integer> oneResultRow = new ArrayList<Integer>();
                    for (k = 1; k <= firstTable.numberOfColumns; k++)
                        oneResultRow.add(firstTable.Records[i][k]);
                    for (k = 1; k <= secondTable.numberOfColumns; k++)
                        oneResultRow.add(secondTable.Records[j][k]);
                    resultRecords.add(oneResultRow);

                }
            }
        }

    }

    public void MatchAndJoinSome(Table firstTable, Table secondTable, Integer firstTableColumnNo,
            Integer secondTableColumnNo, List<Integer> selectedColumns) {
        Integer i, j, k, c;
        resultColumnNames = new ArrayList<String>();
        resultRecords = new ArrayList<List<Integer>>();
        for (k = 0; k < selectedColumns.size(); k++) {
            c = selectedColumns.get(k);
            if (c > 0) { // this column came from first table
                resultColumnNames.add(firstTable.columnNames[c]);
            } else { // this column came from second table
                c = -1 * c;
                resultColumnNames.add(secondTable.columnNames[c]);
            }
        }

        for (i = 1; i <= firstTable.numberOfRecords; i++) {
            for (j = 1; j <= secondTable.numberOfRecords; j++) {
                if (firstTable.Records[i][firstTableColumnNo] == secondTable.Records[j][secondTableColumnNo]) {
                    List<Integer> oneResultRow = new ArrayList<Integer>();
                    for (k = 0; k < selectedColumns.size(); k++) {
                        c = selectedColumns.get(k);
                        if (c > 0) // this column came from first table
                        {
                            oneResultRow.add(firstTable.Records[i][c]);
                        } else // this column came from second table
                        {
                            c = -1 * c;
                            oneResultRow.add(secondTable.Records[j][c]);
                        }
                    }
                    resultRecords.add(oneResultRow);

                }
            }
        }
    }
}
