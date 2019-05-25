import java.util.*;
import java.io.*;

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
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
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

public class MySearch {
	private final static String path = "index/";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner in = new Scanner(System.in);
		File f = new File(path);
		String queryStr = in.next();
		try {
			IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(f)));
			Analyzer analyzer = new IKAnalyzer();
			final String[] searchFields = { "url", "disease", "field", "general", "symptom", "reason", "diagnosis",
					"treatment", "life", "prevention", "docs", "hosp" };
			final String[] fields = { "来源", "病名", "科室", "简介", "症状", "病因", "诊断", "治疗", "生活", "预防", "相关医生", "相关医院" };
			QueryParser parser = new MultiFieldQueryParser(Version.LUCENE_4_10_0, searchFields, analyzer);
			long startTime = System.currentTimeMillis();
			Query query = parser.parse(queryStr);
			TopDocs hits = searcher.search(query, 20);
			System.out.println(
					"共找到" + hits.scoreDocs.length + "条结果，用时" + (System.currentTimeMillis() - startTime) + "ms。");
			for (ScoreDoc doc : hits.scoreDocs) {
				Document d = searcher.doc(doc.doc);
				for (int i = 0; i < fields.length; i++) {
					System.out.println("<--" + fields[i] + "-->");
					String[] buff = d.get(searchFields[i]).split("？");
					if (buff.length == 1)
						System.out.println(d.get(searchFields[i]));
					else {
						for (int j = 0; j < buff.length; j++) {
							if (j == 0) {
								System.out.println(buff[j] + "？");
								continue;
							}
							String[] buffer = buff[j].split("。");
							for (int k = 0; k < buffer.length; k++) {
								System.out.print(buffer[k]);
								if (k + 1 != buffer.length) {
									System.out.println("。");
								} else if (j + 1 == buff.length) {
									System.out.println("。");
								} else {
									System.out.println("？");
								}
							}
						}
					}
				}
				System.out.println("请输入\"##\"继续获取下一组信息，或者按任意键退出");
				String userEnter = in.next();
				if (userEnter.equals("##") == false)
					break;
			}
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
