package pe.pucp.edu.vrp;

import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pe.pucp.edu.vrp.algorithm.Connection;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;
import pe.pucp.edu.vrp.algorithm.Depot;
import pe.pucp.edu.vrp.algorithm.Node;
import pe.pucp.edu.vrp.algorithm.Problem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class VrpApplication {
    public static void main(String[] args) throws Exception {
//		SpringApplication.run(VrpApplication.class, args);
//		System.setOut(new PrintStream("output"));
        Problem problem = new Problem();
        problem.initParams(10);
        List<Node> orderList = new ArrayList<>();
        orderList.add(new Node(1, 16, 3, Region.COAST));
        orderList.add(new Node(2, 3, 1, Region.COAST));
        orderList.add(new Node(3, 10, 3, Region.MOUNTAIN));
        orderList.add(new Node(4, 4, 2, Region.JUNGLE));
        orderList.add(new Node(5, 49, 8, Region.MOUNTAIN));
        orderList.add(new Node(6, 30, 5, Region.COAST));
        orderList.add(new Node(7, 22, 4, Region.JUNGLE));
        orderList.add(new Node(8, 9, 7, Region.JUNGLE));
        orderList.add(new Node(9, 24, 1, Region.MOUNTAIN));
        orderList.add(new Node(10, 32, 2, Region.COAST));
        orderList.add(new Node(11, 12, 5, Region.JUNGLE));
        orderList.add(new Node(12, 5, 4, Region.JUNGLE));
        orderList.add(new Node(13, 44, 1, Region.MOUNTAIN));
        orderList.add(new Node(14, 18, 4, Region.MOUNTAIN));
        orderList.add(new Node(15, 39, 3, Region.JUNGLE));
        orderList.add(new Node(16, 6, 6, Region.COAST));

        //WIP
        List<Connection> connections = new ArrayList<>();
        connections.add(Connection.builder().xIndex(0).yIndex(1).build());

        long start = System.currentTimeMillis();
        double traveled = problem.routeOrders(orderList);
        long finish = System.currentTimeMillis();
        System.out.println("\nElapsed time: " + (finish - start) + " ms");
        System.out.printf("Total time traveled: %3.2f h", traveled);

		//[ARCHIVO OFICINA]
		File ofiTxt = new ClassPathResource("inf226.oficinas.txt").getFile();
		Scanner sc = new Scanner(ofiTxt);
		List<Node> Oficinas = new ArrayList<>();
		List<Depot> Almacenes = new ArrayList<>();
		DecimalFormat df =new DecimalFormat("0.00");
		//Carga de archivos al objeto Oficinas y Almacenes
		int numOfi=0;
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			String[] valores = line.split(",");

			//Cargando los Oficinas
			if(!valores[2].equals("TRUJILLO")&& !valores[2].equals("LIMA")&& !valores[2].equals("AREQUIPA") ){
				Oficinas.add(new Node (numOfi,Integer.parseInt(valores[0]),Double.parseDouble(valores[3]),Double.parseDouble(valores[4]),valores[5]));
			}
			//Cargando los 3 Almacenes Principales
			if(valores[2].equals("TRUJILLO")|| valores[2].equals("LIMA")||valores[2].equals("AREQUIPA") ){
				Almacenes.add(new Depot (numOfi,Integer.parseInt(valores[0]),Double.parseDouble(valores[3]),Double.parseDouble(valores[4])));
			}
			numOfi=numOfi+1;
		}

		//[ARCHIVO TRAMOS]
		File tramosTxt = new ClassPathResource("inf226.tramos.v.2.0.txt").getFile();
		Scanner sctra = new Scanner(tramosTxt);
		double[][] matrixDistancia = new double[numOfi][numOfi];
		Double xIni=0.0,xFin=0.0,yIni=0.0,yFin=0.0;
		int xMatriz=0, yMatriz=0;
		for(int i=0;i<numOfi;i++){
			for(int j=0;j<numOfi;j++){
				if (i==j) matrixDistancia[i][j]= 0.0;
			}
		}
		while (sctra.hasNextLine()) {
			String lineTra = sctra.nextLine();

			String[] valorTra = lineTra.split(" => ");
			valorTra[1]=valorTra[1].substring(0,valorTra[1].length() - 1);//Eliminar último caracter que está en blanco

			for (Depot alma : Almacenes) {
				if (alma.getUbigeo()==Integer.parseInt(valorTra[0])){//punto de tramo inicial
					xIni =alma.getLongitude();
					yIni =alma.getLatitude();
					xMatriz=alma.getMatrixIndex();

				}
				if (alma.getUbigeo()==Integer.parseInt(valorTra[1])){//punto de tramo final
					xFin =alma.getLongitude();
					yFin =alma.getLatitude();
					yMatriz=alma.getMatrixIndex();
				}
			}

			for (Node ofis : Oficinas)  {
				if (ofis.getUbigeo()==Integer.parseInt(valorTra[0])){//punto de tramo inicial
					xIni =ofis.getXCoordinate();
					yIni =ofis.getYCoordinate();
					xMatriz=ofis.getMatrixIndex();

				}
				if (ofis.getUbigeo()==Integer.parseInt(valorTra[1])){//punto de tramo final
					xFin =ofis.getXCoordinate();
					yFin =ofis.getYCoordinate();
					yMatriz=ofis.getMatrixIndex();

				}
			}

			//Colocando la distancia de dos puntos en la matriz
			matrixDistancia[xMatriz][yMatriz]= Double.valueOf(df.format(Math.sqrt(Math.pow(xFin-xIni,2) + Math.pow(yFin-yIni,2))));

		}

		//Prueba de data cargada
		for (int i=0;i<numOfi;i++){
			for (int j=0;j<numOfi;j++){
				System.out.print(String.format("%.2f ",matrixDistancia[i][j]));
			}
			System.out.println();
		}

		//System.out.println("Almacenes:");
		//for (Depot alma : Almacenes) System.out.println(alma.imprimirAlma());

		//System.out.println("Oficinas:");
		//for (Node ofis : Oficinas) System.out.println(ofis.imprimirOfi());

	}
}
