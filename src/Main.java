
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.concurrent.TimeUnit;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Main {
	public static void main(String[] args) throws IOException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.io.File file = new java.io.File("database.txt");
		if (file.exists() == true) {
			System.out.println("database exits");
			System.exit(1);
		}
		java.io.PrintWriter output = new java.io.PrintWriter(file);
		int count = 0;
		final String baseURL = "https://dxy.com/view/i/disease/list?section_group_name=buxian&page_index=";
		MyProcess myProcess = new MyProcess();
		for (int i = 1; i <= 102; i++) {
			String URL = baseURL + String.valueOf(i);
			MyUrl myUrl = new MyUrl(URL);
			String[] urls = myUrl.getUrl();
			for (int j = 1; j < urls.length; j++) {
				if (count++ == 90) {
					try {
						TimeUnit.SECONDS.sleep(605);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					count = 0;
				}
				System.out.println(urls[j]);
				System.out.println(df.format(new Date()));
				Document doc = Jsoup.connect(urls[j])
						.userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)").maxBodySize(0)
						.timeout(600000).get();
				Elements field = doc.select("#container").select("div").select("div.bread-crumb").select("ul")
						.select("li:nth-child(3)").select("a");
				Elements disease = doc.select("#container").select("div").select("div.disease-container")
						.select("div.disease-container-left").select("div:nth-child(1)").select("div").select("div")
						.select("ul").select("li:nth-child(1)").select("div").select("div").select("a");
				Elements general = doc.select("#container").select("div").select("div.disease-container")
						.select("div.disease-container-left").select("div:nth-child(1)").select("div").select("div")
						.select("ul").select("li:nth-child(1)").select("div").select("div")
						.select("div.disease-card-info-content");
				Elements symptom = doc.select("#container").select("div").select("div.disease-container")
						.select("div.disease-container-left").select("div:nth-child(1)").select("div").select("div")
						.select("ul").select("li:nth-child(2)").select("div").select("div.disease-detail-card-deatil");
				Elements reason = doc.select("#container").select("div").select("div.disease-container")
						.select("div.disease-container-left").select("div:nth-child(1)").select("div").select("div")
						.select("ul").select("li:nth-child(3)").select("div").select("div.disease-detail-card-deatil");
				Elements diagnosis = doc.select("#container").select("div").select("div.disease-container")
						.select("div.disease-container-left").select("div:nth-child(1)").select("div").select("div")
						.select("ul").select("li:nth-child(4)").select("div").select("div.disease-detail-card-deatil");
				Elements treatment = doc.select("#container").select("div").select("div.disease-container")
						.select("div.disease-container-left").select("div:nth-child(1)").select("div").select("div")
						.select("ul").select("li:nth-child(5)").select("div").select("div.disease-detail-card-deatil");
				Elements life = doc.select("#container").select("div").select("div.disease-container")
						.select("div.disease-container-left").select("div:nth-child(1)").select("div").select("div")
						.select("ul").select("li:nth-child(6)").select("div").select("div.disease-detail-card-deatil");
				Elements prevention = doc.select("#container").select("div").select("div.disease-container")
						.select("div.disease-container-left").select("div:nth-child(1)").select("div").select("div")
						.select("ul").select("li:nth-child(7)").select("div").select("div.disease-detail-card-deatil");
				Elements doctors = doc.select("#container").select("div").select("div.disease-container")
						.select("div.disease-container-left").select("div:nth-child(2)").select("div.content-info")
						.select("div").select("div.disease-doctor-card");
				String docs = new String();
				for (Element doctor : doctors) {
					Elements doctorDet = doctor.select("a").select("div.doctor-card-name-title");
					docs += doctorDet.text() + "  ";
				}
				Elements hospitals = doc.select("#container").select("div").select("div.disease-container")
						.select("div.disease-container-left").select("div:nth-child(3)").select("div.content-info")
						.select("ul").select("li");
				String hosp = new String();
				for (Element hospital : hospitals) {
					Elements hospitalName = hospital.select("a").select("div.hospital-card-right").select("h3");
					hosp += hospitalName.text() + " ";
				}
				output.println("------" + df.format(new Date()) + "------");
				output.println(urls[j]);
				output.println("disease---------");
				output.println(disease.text());
				output.println("field-----------");
				output.println(field.text());
				output.println("general---------");
				output.println(general.text());
				output.println("symptom---------");
				output.println(symptom.text());
				output.println("reason----------");
				output.println(reason.text());
				output.println("diagnosis----------");
				output.println(diagnosis.text());
				output.println("treatment----------");
				output.println(treatment.text());
				output.println("life----------");
				output.println(life.text());
				output.println("prevention----------");
				output.println(prevention.text());
				output.println("doctors----------");
				output.println(docs);
				output.println("hospitals----------");
				output.println(hosp);
				myProcess.addDoc(urls[j], disease.text(), field.text(), general.text(), symptom.text(), reason.text(),
						diagnosis.text(), treatment.text(), life.text(), prevention.text(), docs, hosp);
			}

		}
		myProcess.endAddDoc();
		output.close();
	}
}
