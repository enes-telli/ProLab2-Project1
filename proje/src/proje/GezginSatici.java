package proje;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GezginSatici {
private static final int NO_PARENT = -1;
    
    static ArrayList<Integer>[] rotaSiralari = new ArrayList[5];
    
    private static int Dijkstra(int[][] komsulukMatrisi, int baslangicSehri, int hedefSehir, int ilkBes) 
    {
        int sehirSayisi = komsulukMatrisi[0].length; 

        int[] enKisaMesafeler = new int[sehirSayisi]; 

        boolean[] eklendiMi = new boolean[sehirSayisi]; 

        for (int sehirIndeksi = 0; sehirIndeksi < sehirSayisi; sehirIndeksi++) 
        { 
            enKisaMesafeler[sehirIndeksi] = Integer.MAX_VALUE; 
            eklendiMi[sehirIndeksi] = false; 
        } 

        enKisaMesafeler[baslangicSehri] = 0; 

        int[] ebeveynler = new int[sehirSayisi]; 

        ebeveynler[baslangicSehri] = NO_PARENT; 

        for (int i = 1; i < sehirSayisi; i++) 
        {
            int enYakinSehir = -1; 
            int enKisaMesafe = Integer.MAX_VALUE;
            
            for (int sehirIndeksi = 0; sehirIndeksi < sehirSayisi; sehirIndeksi++) 
            {
                if (!eklendiMi[sehirIndeksi] && enKisaMesafeler[sehirIndeksi] < enKisaMesafe)  
                { 
                    enYakinSehir = sehirIndeksi; 
                    enKisaMesafe = enKisaMesafeler[sehirIndeksi]; 
                } 
            } 

            eklendiMi[enYakinSehir] = true; 
 
            for (int sehirIndeksi = 0; sehirIndeksi < sehirSayisi; sehirIndeksi++)  
            { 
                int kenarMesafesi = komsulukMatrisi[enYakinSehir][sehirIndeksi]; 
                  
                if (kenarMesafesi > 0 && ((enKisaMesafe + kenarMesafesi) < enKisaMesafeler[sehirIndeksi]))  
                { 
                    ebeveynler[sehirIndeksi] = enYakinSehir; 
                    enKisaMesafeler[sehirIndeksi] = enKisaMesafe + kenarMesafesi;
                } 
            } 
        }
        
        if(ilkBes != -1)
        {
        	RotaHesapla(hedefSehir, ebeveynler, ilkBes);
        }
        
        return enKisaMesafeler[hedefSehir];
    } 

    private static void RotaHesapla(int hedefSehir, int[] ebeveynler, int ilkBes) 
    {
        if (hedefSehir == NO_PARENT) 
        { 
            return; 
        } 
        RotaHesapla(ebeveynler[hedefSehir], ebeveynler, ilkBes);
        rotaSiralari[ilkBes].add(hedefSehir + 1);
    } 

    public static void main(String[] args) throws IOException 
    {
        Scanner sc = new Scanner(System.in);
        int[][] komsulukMatrisi = DosyadanOkuma(81, 81, "matris.txt");
        int[][] koordinatlar = DosyadanOkuma(81, 2, "koordinatlar.txt");
        
        System.out.print("Kac sehir girmek istiyorsunuz: ");
        
        int sehirSayisi = sc.nextInt();
        int[] sehirler = new int[sehirSayisi];
        
        System.out.println("\nKocaeli sehri otomatik olarak rotaya eklenmistir.");
        System.out.println("Lutfen Kocaeli sehrini girmeyiniz!");
        System.out.println("Lutfen isim ile degil plaka kodu ile giris yapiniz.\n");
        
        for(int i = 0; i < sehirler.length; i++)
        {
        	System.out.print((i + 1) + ". sehir: ");
        	sehirler[i] = sc.nextInt() - 1;
        }
        
        Permutasyon perm = new Permutasyon(sehirler);
        perm.IlkiniAl();
        
        int faktoriyel = (sehirSayisi > 8) ? Faktoriyel(8) : Faktoriyel(sehirSayisi);
        
        int[] enKisaMesafeler = new int[5];
        
        for(int i = 0; i < 5; i++)
        {
        	enKisaMesafeler[i] = Integer.MAX_VALUE;
        }
        
        int mesafe;
        
        int[][] enKisaRotalar = new int [5][sehirSayisi];
        
        for(int i = 0; i < faktoriyel; i++)
        {
        	mesafe = 0;
        	
        	mesafe += Dijkstra(komsulukMatrisi, 40, perm.dizi[perm.indeksler[0]], -1);
        	
        	for(int j = 0; j < sehirler.length - 1; j++)
        	{
        		mesafe += Dijkstra(komsulukMatrisi, perm.dizi[perm.indeksler[j]], perm.dizi[perm.indeksler[j + 1]], -1);
        	}
        	
        	mesafe += Dijkstra(komsulukMatrisi, perm.dizi[perm.indeksler[perm.dizi.length - 1]], 40, -1);
        	
        	EnKisaYollariSirala(enKisaMesafeler, mesafe, enKisaRotalar, perm);
        	
        	if(i == faktoriyel - 1)
        		break;
        	
        	perm.SonrakiniAl();
        }
        
        System.out.println("");
        
        for (int i = 0; i < 5; i++)
        { 
            rotaSiralari[i] = new ArrayList<Integer>();
        }
        
        for(int i = 0; i < 5; i++)
        {
        	if(enKisaMesafeler[i] == Integer.MAX_VALUE)
        		break;
        	
        	Dijkstra(komsulukMatrisi, 40, enKisaRotalar[i][0], i);
        	rotaSiralari[i].remove(rotaSiralari[i].size() - 1);
        	
        	for(int j = 0; j < sehirler.length - 1; j++)
        	{
        		Dijkstra(komsulukMatrisi, enKisaRotalar[i][j], enKisaRotalar[i][j + 1], i);
        		rotaSiralari[i].remove(rotaSiralari[i].size() - 1);
        	}
        	
        	Dijkstra(komsulukMatrisi,enKisaRotalar[i][sehirler.length - 1], 40, i);
        }
        
        for(int i = 0; i < 5; i++)
        {
        	if(enKisaMesafeler[i] == Integer.MAX_VALUE)
        		break;
        	
        	System.out.println("En kisa " + (i + 1) + ". yol " +enKisaMesafeler[i] + "km'dir.");
        	System.out.print("Rota: ");
        	
        	for(int j = 0; j < rotaSiralari[i].size() - 1; j++)
        	{
        		System.out.print(rotaSiralari[i].get(j) + " -> ");
        	}
        	
        	System.out.println(rotaSiralari[i].get(rotaSiralari[i].size() - 1) + "\n");
        }
        
        String resimYolu = "harita.jpg";
        BufferedImage[] resim = new BufferedImage[5];
        
        for(int i = 0; i < 5; i++)
        {
        	resim[i] = ImageIO.read(new File(resimYolu));
        }
    	
    	Graphics2D[] g = new Graphics2D[5];
    	
    	for(int i = 0; i < 5; i++)
    	{
    		g[i] = (Graphics2D) resim[i].getGraphics();
    		g[i].setStroke(new BasicStroke(3));
    		g[i].setColor(Color.BLUE);
    	}
    	
    	for(int i = 0; i < 5; i++)
    	{
    		if(enKisaMesafeler[i] == Integer.MAX_VALUE)
        		break;
    	    
    		g[i].drawLine(koordinatlar[40][0], koordinatlar[40][1], 
    			          koordinatlar[rotaSiralari[i].get(0) - 1][0], koordinatlar[rotaSiralari[i].get(0) - 1][1]);
    	
    		for(int j = 0; j < rotaSiralari[i].size() - 1; j++)
    		{
    			g[i].drawLine(koordinatlar[rotaSiralari[i].get(j) - 1][0], koordinatlar[rotaSiralari[i].get(j) - 1][1],
    			              koordinatlar[rotaSiralari[i].get(j + 1) - 1][0], koordinatlar[rotaSiralari[i].get(j + 1) - 1][1]);
    		}
    	
    		g[i].drawLine(koordinatlar[rotaSiralari[i].get(rotaSiralari[i].size() - 1) - 1][0],
    			          koordinatlar[rotaSiralari[i].get(rotaSiralari[i].size() - 1) - 1][1],
    			          koordinatlar[40][0],
    			          koordinatlar[40][1]);
    	}
    	
    	JLabel[] jLabels = new JLabel[5];
    	JPanel[] jPanels = new JPanel[5];
    	JFrame[] jFrames = new JFrame[5];
    	
    	for(int i = 0; i < 5; i++)
    	{
    		if(enKisaMesafeler[i] == Integer.MAX_VALUE)  // 3 girdi icin: genelde 2 tane yol buldugundan ilk 5 yolun kalan 3 ü sonsuz oluyor 
        		break;
    		
    		jLabels[i] = new JLabel(new ImageIcon(resim[i]));
    		jPanels[i] = new JPanel();
    		jPanels[i].add(jLabels[i]);
    		jFrames[i] = new JFrame("En Kisa " + (i + 1) + ". Yol");
    		jFrames[i].setSize(new Dimension(resim[i].getWidth(), resim[i].getHeight()));
        	jFrames[i].add(jPanels[i]);
        	jFrames[i].setVisible(true);
    	}
    }
    
    public static void EnKisaYollariSirala(int[] mesafeler, int mesafe, int[][] enKisaRotalar, Permutasyon perm)
    {
    	for(int i = 0; i < 5; i++)
    	{
    		if(mesafe == mesafeler[i])
    			return;
    		
    		if(mesafe < mesafeler[i])
    		{
    			if(i == 4)
    			{
    				mesafeler[i] = mesafe;
    				
    				for(int j = 0; j < perm.dizi.length; j++)
    				{
    					enKisaRotalar[i][j] = perm.dizi[perm.indeksler[j]];
    				}
    				return;
    			}
    			
    			for(int j = 3; j >= i; j--)
    			{
    				mesafeler[j + 1] = mesafeler[j];
    				
    				for(int k = 0; k < perm.dizi.length; k++)
    				{
    					enKisaRotalar[j + 1][k] = enKisaRotalar[j][k];
    				}
    			}
    			
    			mesafeler[i] = mesafe;
    			
    			for(int j = 0; j < perm.dizi.length; j++)
				{
					enKisaRotalar[i][j] = perm.dizi[perm.indeksler[j]];
				}
    			return;
    		}
    	}
    	return;
    }
    
    public static int Faktoriyel(int number)
    {
        if(number == 1)
            return 1;
        else
            return number * Faktoriyel(number - 1);
    }
    
    public static int[][] DosyadanOkuma(int satir, int sutun, String dosyaYolu) throws FileNotFoundException, NumberFormatException
    {
        Scanner sc = new Scanner(new BufferedReader(new FileReader(dosyaYolu)));
        int [][] dizi = new int[satir][sutun];
        
        while(sc.hasNextLine()) {
            for (int i=0; i<dizi.length; i++) {
                String[] line = sc.nextLine().trim().split(" ");
                for (int j=0; j<line.length; j++) {
                    dizi[i][j] = Integer.parseInt(line[j]);
                }
            }
        } 
        return dizi;
    }
}
