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
public class Kehitysapu {
    
    // Globalis!
    public Kehitysapu(){
        
    }
// Globaliksesta n‰hd‰‰n, ett‰ viime aikoina Somalina kehitysapu on noussut vahvasti.
// Vuonna 1993 oltiin korkealla koska sinne tuli paljon sotavoimia, kuten myˆs 2008.
// Kehitysavun ylitt‰ess‰ 500 miljoonaa ollaan l‰hes aina oltu vakavassa tilanteessa
// sotilaallisesti (sis‰llissota tms.), eli sen voisi ottaa mukaan koodiin huomioimaan tilannetta.

// Yhdysvaltojen inflaation ennustetaan kasvavan yhdest‰, mutta se ei
// todenn‰kˆisesti ylit‰ 2 prosenttia. Siisp‰ voidaan muuttaa inflaatiotakin
// samalla tavalla kuin taloutta, ja n‰m‰ muuttujat voidaan laittaa yhteyteen
// toistensa kanssa niinkuin oikeassakin taloudessa tapahtuu. Euroopassa ollaan
// alle yhdess‰ prosentissa mutta siell‰kin pyrit‰‰n pysym‰‰n alle kahdessa ja
// yli yhdess‰ prosentissa. Aasian inflaatiota ei ole otettu mallissa huomioon,
// koska kehitysapua annetaan Aasiasta liian v‰h‰n sen vaikuttavan merkitt‰v‰sti.
// DONE (Osittain at least)

// Kehitys taloudessa on t‰ll‰ hetkell‰ menossa kohti tilannetta, jossa Eurooppa
// ja Yhdysvallat menett‰v‰t osuuttaan maailman BKT:sta Kiinalle, Japanille, Intialle
// ja muille Aasian maille, unohtamatta Afrikan kasvua. Mallin pit‰‰ siis huomioida
// se, ett‰ tulevaisuudessa BKT ei v‰ltt‰m‰tt‰ kasva samalla tavalla. Voidaan kuitenkin
// olettaa ett‰ se ei VƒHENNƒ BKT:n kasvua muualla; se vain hidastaa kasvua
// http://nextbigfuture.com/2013/05/countries-by-world-gdp-in-2025.html
// J‰rjestys on siis vuonna 2025 Kiina, Japani, Intia, Ven‰j‰, Brasilia.
// DONE (osittain at least)

// Konfliktin todenn‰kˆisyys maassa ei ole kovin korkea, sill‰ t‰ll‰ hetkell‰ maan
// tilanne on paranemaan p‰in. Pit‰‰ kuitenkin huomioida ett‰ erityisesti Etel‰-
// Somaliassa on terrorismia joka aiheuttaa jopa katutaisteluita, mink‰ vuoksi
// tilanne on edelleen melko siet‰m‰tˆn. Lis‰ksi ilmastonmuutos! -> N‰lk‰
// Lis‰ksi konfliktin alkaessa sen jatkumisen todenn‰kˆisyys kasvaa valtavasti,
// sill‰ konfliktit harvoin ovat lyhyit‰, ainakaan Somaliassa. T‰m‰n vuoksi mallin
// pit‰‰ pit‰‰ kehitysapua korkeana kunnes konflikti lakkaa. Tarvitaan siis toden-
// n‰kˆisyys sek‰ konfliktin alulle ett‰ sen loppumiselle (alku pienempi merkitt‰v‰sti)
// DONE (osittain at least)

// Ilmastonmuutoksen tulisi kasvattaa riski‰ luonnonkatastrofiin, mutta koska
// yksi ajokerta vastaa vuotta, kasvun pit‰‰ olla eritt‰in, eritt‰in pient‰
// (mahdollisesti luokkaa 1.0*10^-5 tai v‰hint‰‰n alkaa t‰st‰ arvosta) ja t‰m‰n
// katastrofin pit‰‰ jotenkin pahentaa n‰l‰nh‰t‰‰ (tulvat nyt aiheuttavat v‰h‰n
// v‰li‰ alueella paikoittaista n‰l‰nh‰t‰‰). T‰m‰ kasvattaa/v‰hent‰‰ kehitysapua
// http://blogs.helsinki.fi/parikka/2007/11/28/oxfam-luonnon-onnettomuuksien-maara-moninkertaistunut/
// ^Linkki jossa kerrotaan siit‰, ett‰ nyky‰‰n tapahtuu n. 500 onnettomuutta/vuosi
// DONE (osittain at least)

// V‰estˆn kasvu lis‰‰ ihmisten m‰‰r‰‰ ja samalla ruokittavia suita. T‰m‰ tarkoittaa
// mahdollisuutta muiden maiden ruokakriiseihin, jolloin ne pit‰‰ hoitaa ensin.
// T‰m‰ v‰hent‰‰ t‰ten Somaliaan tulevaa kehitysapua jonkin verran.
// YK arvioi v‰estˆnkasvun kulkevan vauhdilla 2020 = 7,6 mrd, 2030 = 8,3 mrd,
// 2040 = 8,8 mrd, 2050 = 9,3 mrd, 2060 = 9,6 mrd, 2070 = 9,8 mrd, 2080 = (9,96mrd)10 mrd
// 2090 = (10,06mrd) 10 mrd, 2100 = 10,1 mrd. Mallissa vain Afrikan, P-Amerikan ja Oseanian v‰kiluku
// Kasvaa koko 21. vuosisadan, Aasiankin v‰kiluku k‰‰ntyy laskuun 2050 ja E-Amerikan 2060
// Euroopan v‰kiluvun ollessa aina 2020 l‰htien laskussa. Koodin pit‰‰ siis ottaa huomioon,
// kuinka monta kierrosta on jo koodia mennyt ja siten kasvattaa ruokaongelmien todenn‰kˆisyytt‰
// Huomioitavaa on myˆs syyt ruokakriisille: biopolttoaineet, aasian vaurastuminen, kuivuus ja keinottelu
// FAO.org ja FAO foodprice index

// HUOM! Kehitysapuun on laskettu se, ett‰ joka vuosi jossain p‰in maailmaa tapahtuu
// Paha katastrofi johonn ohjataan rahaa. T‰ss‰ mallissa kuitenkin huomioidaan
// mahdollisuus useampaan pahaan onnettomuuteen, jolloin Somaliaan saapuva avustus
// v‰henee jo(i)llakin promillen osa(/i)lla
// DONE (osittain at least)

// Valtionvelan kasvu jatkunee kaikissa maailman valtioissa kohisten. T‰m‰ voi johtaa
// kriiseihin, sill‰ esimerkiksi japanilla on enemm‰n velkaa kuin BKT:ta. USA:n
// valtiovelka on n. 73% BKT:sta, mik‰ USA:n BKT:lla on valtava summa. Riskej‰
// ovat inflaatio ja talouden kasvun heikentyminen.

	 public static void KehitysAvunLaskenta(){
                Random random=new Random();
		double kehitysapu = 662000000;
		double xx = 0;
		double x = 0.5;
		int i = 0;
		int onnettomuudet = 0;
		int katastrofit = 0;
		double inflaatio = 1.0;
		int konflikti = 2;
		int kesto = 0;
		double ilmastonmuutos = 0.01;
		double nalanhata = 0.001;
		double z = 0.9;
		double bktkasvukerroin = 0.00001913;
		double bktlaskukerroin = 0.00001838;
		
		while (i<10){
			double randomi = random.nextDouble();
			i = i + 1;
			
			if ((konflikti == 1) && (z > randomi)){
				kehitysapu = kehitysapu + kehitysapu*(2/(konflikti));
				onnettomuudet = onnettomuudet + 1;
				konflikti = 2;
				z = 0.6;
				kesto = kesto + 1;
				}
			else if (konflikti == 2){
				if (randomi > z){
					konflikti = 1;
					kehitysapu = kehitysapu / 2;
					z = 0.1;
					}
				}
			else if (x >= randomi){
				kehitysapu = kehitysapu + kehitysapu*bktkasvukerroin;
				xx = x;
				x = x - 0.1;
				}	
			else if (x < randomi){
				kehitysapu = kehitysapu - kehitysapu*bktlaskukerroin;
				xx = x;
				x = x + 0.1;
				}	
			else if (inflaatio > 2.0){
				randomi = random.nextDouble();
				inflaatio = inflaatio * randomi;
				kehitysapu = kehitysapu - kehitysapu*0.01*inflaatio;
				}	
			else if (inflaatio < 2.0){
				inflaatio = inflaatio + randomi;
				}
			else if(1==0){
				nalanhata = nalanhata + ilmastonmuutos;
				if (nalanhata > randomi){
					kehitysapu = kehitysapu + kehitysapu*0.2;
					}
				}
			else if(1==0){
//				bktkasvukerroin = bktkasvukerroin - taloudenmuutoskerroin;
//				bktlaskukerroin = bktlaskukerroin - taloudenmuutoskerrroin;
//				taloudenmuutoskerroin = Math.pow(taloudenmuutoskerroin, (1+taloudenmuutoskerroin));

				}
			}
		System.out.println ("Kehitysapua oli lopussa "+ kehitysapu + " dollaria. Somaliassa onnettomuuksia tapahtui "+ onnettomuudet + " kpl ja muualla "+ katastrofit+" kpl.");
		
		}
}
