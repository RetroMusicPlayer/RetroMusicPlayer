package code.name.monkey.retromusic.activities.bugreport.model.github;

public class GithubTarget {

  private final String repository;

  private final String username;

  public GithubTarget(String username, String repository) {
    this.username = username;
    this.repository = repository;
  }

  public String getRepository() {
    return repository;
  }

  public String getUsername() {
    return username;
  }
}
