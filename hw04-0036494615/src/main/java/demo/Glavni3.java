package demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.LSystemBuilderProvider;
import hr.fer.zemris.lsystems.gui.LSystemViewer;
import hr.fer.zemris.lsystems.impl.LSystemBuilderImpl;

public class Glavni3 {
	
	public static void main(String[] args) throws FileNotFoundException {
//		String filePath = "/home/john/java-zadace/hw04-0036494615/res/kochIsland.txt";
		
		LSystemViewer.showLSystem(LSystemBuilderImpl::new);
		}
	
	private static LSystem createCurve(LSystemBuilderProvider provider, String filePath) throws FileNotFoundException {
		LSystemBuilder b = provider.createLSystemBuilder();
		String data[] = new String[1];
		
		/* Rjesenje je smotano jer ne smijemo koristiti Kolekcije */
		Scanner sc = new Scanner(new File(filePath));
		
		while (sc.hasNext()) {
			data[0] = sc.nextLine();
			b.configureFromText(data);
		}
		
		sc.close();
		return b.build();
	}

}
