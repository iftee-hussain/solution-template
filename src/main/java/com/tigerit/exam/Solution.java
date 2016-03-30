package com.tigerit.exam;



import static com.tigerit.exam.IO.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * All of your application logic should be placed inside this class.
 * Remember we will load your application from our custom container.
 * You may add private method inside this class but, make sure your
 * application's execution points start from inside run method.
 */
public class Solution implements Runnable {
	
	class Table {
		private String name;
		private ArrayList<String> columnName;
		private HashMap<String, Integer>colSerial = new HashMap<>();
		private ArrayList<ArrayList<Integer>> data;


		protected Table(String name, ArrayList<String> columnName) {
			this.name = name;
			this.columnName = columnName;
			for(int i=0; i<columnName.size(); i++){
				colSerial.put(columnName.get(i), i);
			}
			data = new ArrayList<ArrayList<Integer>>();
		}

		public void addRow(ArrayList<Integer> rowData) {
			data.add(rowData);
		}

		public int getRecordCount() {
			return data.size();
		}

		public String getTableName() {
			return name;
		}

		public ArrayList<String> getColumnNames() {
			return columnName;
		}

		public int getColumnPosition(String col) {
			return colSerial.get(col);
		}

		public ArrayList<ArrayList<Integer>> getData() {
			return data;
		}

	}
	
	

class Pair {
	public String tableName;
	public String columnName;

	public Pair(String tName, String colName) {
		this.tableName = tName;
		this.columnName = colName;
	}
}

	private HashMap<String, Table> tables;
    @Override
    public void run() {
        // your application entry point

        // sample input process
    	tables = new HashMap<>();
    	int testCase = readLineAsInteger();
    	for (int tc = 1; tc <= testCase; tc++) {
    		printLine("Test: "+ tc);
    		takeInput();
    		int noOfQuery = readLineAsInteger();
    		for (int q = 0; q < noOfQuery; q++) {
				//readLine();
				ArrayList<String> queryStr = new ArrayList<>();
				for (int i = 0; i < 4; i++) {
					queryStr.add(readLine());
				}
				readLine();
				String tab1[] = queryStr.get(1).split("\\s+");
				String tab2[] = queryStr.get(2).split("\\s");
				ArrayList<String> qTable1Name = new ArrayList<>();
				ArrayList<String> qTable2Name = new ArrayList<>();

				if (tab1.length == 2) {
					qTable1Name.add(tab1[1]);
					qTable1Name.add(tab1[1]);
					qTable2Name.add(tab2[1]);
					qTable2Name.add(tab2[1]);
				} else {
					qTable1Name.add(tab1[1]);
					qTable1Name.add(tab1[2]);
					qTable2Name.add(tab2[1]);
					qTable2Name.add(tab2[2]);
				}
				Table selectedTable[] = { tables.get(qTable1Name.get(0)), tables.get(qTable2Name.get(0)) };

				String selectedSTMT[] = queryStr.get(0).replaceAll(",", " ").replaceAll("\\s+", " ").trim().split(" ");

				ArrayList<Pair> selectedRow = new ArrayList<>();

				if (selectedSTMT[1].equals("*")) {
					ArrayList<String> t1 = tables.get(qTable1Name.get(0)).getColumnNames();
					ArrayList<String> t2 = tables.get(qTable2Name.get(0)).getColumnNames();
					for (int i = 0; i < t1.size(); i++) {
						selectedRow.add(new Pair(qTable1Name.get(0), t1.get(i)));
					}
					for (int i = 0; i < t2.size(); i++) {
						selectedRow.add(new Pair(qTable2Name.get(0), t2.get(i)));
					}

				} else {
					for (int i = 1; i < selectedSTMT.length; i++) {
						String str[] = selectedSTMT[i].split("\\.");
						if (str[0].equals(qTable1Name.get(0)) || str[0].equals(qTable1Name.get(1))) {
							selectedRow.add(new Pair(qTable1Name.get(0), str[1]));
						} else {
							selectedRow.add(new Pair(qTable2Name.get(0), str[1]));
						}
					}
				}
				String cond[] = queryStr.get(3).replaceAll("=", " ").replaceAll("\\s+", " ").trim().split(" ");
				String conditions[] = new String[2];
				for (int i = 1; i <= 2; i++) {
					String str[] = cond[i].split("\\.");
					if (str[0].equals(qTable1Name.get(0)) || str[0].equals(qTable1Name.get(1))) {
						conditions[0] = str[1];
					} else {
						conditions[1] = str[1];
					}
				}

				int n1 = selectedTable[0].getRecordCount();
				int n2 = selectedTable[1].getRecordCount();
				ArrayList<ArrayList<Integer>> data1 = selectedTable[0].getData();
				ArrayList<ArrayList<Integer>> data2 = selectedTable[1].getData();
				int noOfColsOfTable[] = { selectedTable[0].getColumnPosition(conditions[0]),
						selectedTable[1].getColumnPosition(conditions[1]) };

				ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
				for (int i = 0; i < n1; i++) {
					ArrayList<Integer> temp = new ArrayList<>();
					for (int j = 0; j < n2; j++) {
						if (data1.get(i).get(noOfColsOfTable[0]) == data2.get(j).get(noOfColsOfTable[1])) {

							for (int k = 0; k < selectedRow.size(); k++) {
								if (selectedRow.get(k).tableName.equals(selectedTable[0].getTableName())) {
									temp.add(data1.get(i)
											.get(selectedTable[0].getColumnPosition(selectedRow.get(k).columnName)));
								} else {
									temp.add(data2.get(i)
											.get(selectedTable[1].getColumnPosition(selectedRow.get(k).columnName)));
								}
							}
							result.add(temp);
						}
					}

				}
				Collections.sort(result, new Comparator<ArrayList<Integer>>() {

					@Override
					public int compare(ArrayList<Integer> o1, ArrayList<Integer> o2) {
						// TODO Auto-generated method stub

						for (int i = 0; i < o1.size(); i++) {
							if (o1.get(i) == o2.get(i))
								continue;
							if (o1.get(i) < o2.get(i))
								return -1;
							return 1;
						}
						return 0;
					}
				});

				for (int i = 0; i < selectedRow.size(); i++) {
					if (i != 0)
						System.out.print(" ");
					System.out.print(selectedRow.get(i).columnName);
				}
				System.out.println();
				for (int i = 0; i < result.size(); i++) {
					for (int j = 0; j < result.get(i).size(); j++) {
						if (j != 0)
							System.out.print(" ");

						System.out.print(result.get(i).get(j));
					}
					System.out.println();
				}
				System.out.println();
				
			}
    	}
        // sample output process
        /*printLine(string);
        printLine(integer);*/
    }

    
    private void takeInput() {
		int noOfTable = readLineAsInteger();
		for (int i = 0; i < noOfTable; i++) {
			String tableName = readLine();
			String [] rowColumn = readLine().split(" ");
			int noOfColumn = Integer.parseInt(rowColumn[0]);
			int noOfRecord =  Integer.parseInt(rowColumn[1]);
			ArrayList<String> colName = new ArrayList<>();
			String [] columns = readLine().split(" ");
			for (int c = 0; c < noOfColumn; c++) {
				colName.add(columns[c]);
			}
			Table table = new Table(tableName, colName);

			for (int r = 0; r < noOfRecord; r++) {
				String [] record = readLine().split(" ");
				ArrayList<Integer> records = new ArrayList<>();
				for (int c = 0; c < noOfColumn; c++) {
					records.add(Integer.parseInt(record[c]));
				}
				table.addRow(records);
			}
			tables.put(tableName, table);
		}
    }
		
		
}

