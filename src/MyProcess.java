import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;
import java.io.*;

public class MyProcess {
	private final String path = "index/";
	private File f;
	private IndexWriter iwr;

	public MyProcess() {
		f = new File(path);
		if (f.exists()) {
			System.out.println("index exits");
			System.exit(1);
		}
		iwr = null;
		try {
			Directory dir = FSDirectory.open(f);
			Analyzer analyzer = new IKAnalyzer();
			IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_4_10_0, analyzer);
			iwr = new IndexWriter(dir, conf);// 建立IndexWriter。固定套路
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addDoc(String url, String disease, String field, String general, String symptom, String reason,
			String diagnosis, String treatment, String life, String prevention, String docs, String hosp) {
		try {
			Document doc = getDocument(url, disease, field, general, symptom, reason, diagnosis, treatment, life,
					prevention, docs, hosp);
			iwr.addDocument(doc);// 添加doc，Lucene的检索是以document为基本单位
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void endAddDoc() {
		try {
			iwr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Document getDocument(String url, String disease, String field, String general, String symptom,
			String reason, String diagnosis, String treatment, String life, String prevention, String docs,
			String hosp) {
		Document doc = new Document();
		Field urlField = new StringField("url", url, Field.Store.YES);
		Field disField = new TextField("disease", disease, Field.Store.YES);
		Field fieField = new TextField("field", field, Field.Store.YES);
		Field genField = new TextField("general", general, Field.Store.YES);
		Field symField = new TextField("symptom", symptom, Field.Store.YES);
		Field reaField = new TextField("reason", reason, Field.Store.YES);
		Field diaField = new TextField("diagnosis", diagnosis, Field.Store.YES);
		Field treField = new TextField("treatment", treatment, Field.Store.YES);
		Field lifeField = new TextField("life", life, Field.Store.YES);
		Field preField = new TextField("prevention", prevention, Field.Store.YES);
		Field docsField = new TextField("docs", docs, Field.Store.YES);
		Field hospField = new TextField("hosp", hosp, Field.Store.YES);
		doc.add(urlField);
		doc.add(disField);
		doc.add(fieField);
		doc.add(genField);
		doc.add(symField);
		doc.add(reaField);
		doc.add(diaField);
		doc.add(treField);
		doc.add(lifeField);
		doc.add(preField);
		doc.add(docsField);
		doc.add(hospField);
		return doc;
	}
}
