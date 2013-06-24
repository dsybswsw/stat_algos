package driver.epinions;

/**
 * User: dawei, dsybswsw@gmail.com
 * Date: 6/20/13
 */
public class EpinionsDataSet {
	private String rootDir;
	private String userRelationFile;
	private String authorArticleFile;
	private String ratingsFile;

	private EpinionsDataSet() {
		// Set the default values.
		this.rootDir = "./data/epinions/";
		this.userRelationFile = rootDir + "user_rating.txt";
		this.authorArticleFile = rootDir + "mc.txt";
		this.ratingsFile = rootDir + "rating.txt";
	}

	public static EpinionsDataSet getInstance() {
		return new EpinionsDataSet();
	}

	public String getRootDir() {
		return rootDir;
	}

	public String getUserRelationFile() {
		return userRelationFile;
	}

	public String getAuthorArticleFile() {
		return authorArticleFile;
	}

	public String getRatingsFile() {
		return ratingsFile;
	}
}
