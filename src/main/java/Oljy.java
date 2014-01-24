import java.util.Scanner;
import java.util.Random;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Shaqqy
 */
public class Oljy {
    public Oljy(){
    }

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Shaqqy
 */

        // TODO code application logic here
        
    
static float oljy(){
Scanner kysy=new Scanner(System.in);
Random random=new Random();
//Öljykriisejä on tapahtunut noin kymmenen vuoden välein,
//joten oletamme öljykriisin todennäköisyydeksi vuosittain 0.1.
//Öljyn tuotannon täytyy pysyä sen kysynnän mukana, joka kasvaaa jatkuvasti.
//Jos vanhat lähteet tyrehtyvät ja uusia ei löydetä tilalle, öljyn hinta nousee, joka vaikuttaa maailmantalouteen huiomattavasti.
int kehitysapu=662000000; //Somalian saaman kehitysavun määrä vuonna 2009, jolloin oli käynnissä 2008-2009
//ennätysnopea öljyn hinnan nousu tuotannon riittämättömän nousun suhteessa kysyntään takia.
 //lähtökohta vuosille joilla mallia ajetaan
float öljy=60;//Öljyn kekimääräinen hinta vuonna 2009 usd/bbl
System.out.println("Mihin vuoteen asti ajetaan?");
int vuosi = kysy.nextInt(); //malli kysyy käyttäjältä mihin vuoteen asti peliä pelataan
for (int i=2009; i<vuosi;i++){    //vuosi on se vuosi, mihin asti mallin halutaan ajavan. Voidaan valita mielivaltaisesti.
    int öljykriisi=random.nextInt(10);
    int nousulasku=random.nextInt(2);
    double lol=random.nextDouble();
    
    if (öljykriisi == 0){
        nousulasku=3;
                }
    if (nousulasku==0){
        öljy += (lol*4);}
    else if(nousulasku==1){
        öljy += (lol*(-4));
        }
    else{
        öljy += (lol*6);
                }

        }
    return öljy;   
    }
}

