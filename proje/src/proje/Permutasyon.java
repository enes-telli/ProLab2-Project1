package proje;

public class Permutasyon {
	public final int dizi[];
    public int indeksler[];
    public int artis; 

    public Permutasyon(int arr[]) 
    { 
        this.dizi = arr; 
        this.artis = -1; 
        this.indeksler = new int[this.dizi.length]; 
    } 

    public void IlkiniAl() 
    { 
 
        this.indeksler = new int[this.dizi.length]; 

        for (int i = 0; i < indeksler.length; ++i)  
        { 
            this.indeksler[i] = i; 
        } 

        this.artis = 0;
    } 

    public void SonrakiniAl() 
    { 

        if (this.artis == 0)  
        {
            this.YerDegistir(this.artis, this.artis + 1); 

            this.artis += 1; 
            while (this.artis < this.indeksler.length - 1 && this.indeksler[this.artis] > this.indeksler[this.artis + 1])  
            { 
                ++this.artis; 
            } 
        } 
        else
        {
            if (this.indeksler[this.artis + 1] > this.indeksler[0])  
            { 
                this.YerDegistir(this.artis + 1, 0); 
            } 
            else
            {
                int start = 0; 
                int end = this.artis; 
                int mid = (start + end) / 2; 
                int tVal = this.indeksler[this.artis + 1]; 
                while (!(this.indeksler[mid]<tVal&& this.indeksler[mid - 1]> tVal))  
                { 
                    if (this.indeksler[mid] < tVal) 
                    { 
                        end = mid - 1; 
                    } 
                    else 
                    { 
                        start = mid + 1; 
                    } 
                    mid = (start + end) / 2; 
                } 

                this.YerDegistir(this.artis + 1, mid); 
            } 

            for (int i = 0; i <= this.artis / 2; ++i) 
            { 
                this.YerDegistir(i, this.artis - i); 
            } 

            this.artis = 0; 
        }
    }
    
    private void YerDegistir(int p, int q) 
    { 
        int gecici = this.indeksler[p]; 
        this.indeksler[p] = this.indeksler[q]; 
        this.indeksler[q] = gecici; 
    }
}
