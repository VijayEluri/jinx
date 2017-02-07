package net.jeremybrooks.jinx.api;

import net.jeremybrooks.jinx.Jinx;
import net.jeremybrooks.jinx.JinxException;
import net.jeremybrooks.jinx.JinxUtils;
import net.jeremybrooks.jinx.response.galleries.GalleryInfo;
import net.jeremybrooks.jinx.response.urls.GroupUrls;
import net.jeremybrooks.jinx.response.urls.UserUrls;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Jeremy Brooks
 */
public class UrlsApi {
  private Jinx jinx;

  private UrlsApi() {
  }

  public UrlsApi(Jinx jinx) {
    this.jinx = jinx;
  }

  /**
   * Returns the url to a group's page.
   *
   * This method does not require authentication.
   *
   * @param groupId NSID of the group to fetch the url for. Required.
   * @return group NSID and URL.
   * @throws JinxException if groupId parameter is missing, or if there are any errors.
   * @see <a href="https://www.flickr.com/services/api/flickr.urls.getGroup.html">flickr.urls.getGroup</a>
   */
  public GroupUrls getGroup(String groupId) throws JinxException {
    JinxUtils.validateParams(groupId);
    Map<String, String> params = new TreeMap<>();
    params.put("method", "flickr.urls.getGroup");
    params.put("group_id", groupId);
    return jinx.flickrGet(params, GroupUrls.class, false);
  }


  /**
   * Returns the url to a user's photos.
   *
   * This method does not require authentication.
   *
   * @param userId user id (NSID) of the user to fetch the url for. If omitted, the calling user is assumed. Optional.
   * @return user id and rl to the users photos.
   * @throws JinxException if there are any errors.
   * @see <a href="https://www.flickr.com/services/api/flickr.urls.getUserPhotos.html">flickr.urls.getUserPhotos</a>
   */
  public UserUrls getUserPhotos(String userId) throws JinxException {
    Map<String, String> params = new TreeMap<>();
    params.put("method", "flickr.urls.getUserPhotos");
    if (!JinxUtils.isNullOrEmpty(userId)) {
      params.put("user_id", userId);
    }
    return jinx.flickrGet(params, UserUrls.class);
  }

  /**
   * Returns the url to a user's profile.
   *
   * This method does not require authentication.
   *
   * @param userId user id (NSID) of the user to fetch the url for. If omitted, the calling user is assumed. Optional.
   * @return user id and url to the users profile.
   * @throws JinxException if there are any errors.
   * @see <a href="https://www.flickr.com/services/api/flickr.urls.getUserProfile.html">flickr.urls.getUserProfile</a>
   */
  public UserUrls getUserProfile(String userId) throws JinxException {
    Map<String, String> params = new TreeMap<>();
    params.put("method", "flickr.urls.getUserProfile");
    if (!JinxUtils.isNullOrEmpty(userId)) {
      params.put("user_id", userId);
    }
    return jinx.flickrGet(params, UserUrls.class);
  }

  /**
   * Returns gallery info, by url.
   *
   * This method does not require authentication.
   *
   * @param url gallery URL to look up. Required.
   * @return information about the gallery.
   * @throws JinxException if required parameters are missing, or if there are any errors.
   * @see <a href="https://www.flickr.com/services/api/flickr.urls.lookupGallery.html">flickr.urls.lookupGallery</a>
   */
  public GalleryInfo lookupGallery(String url) throws JinxException {
    JinxUtils.validateParams(url);
    Map<String, String> params = new TreeMap<>();
    params.put("method", "flickr.urls.lookupGallery");
    params.put("url", url);
    return jinx.flickrGet(params, GalleryInfo.class);
  }

  /**
   * Returns a group NSID, given the url to a group's page or photo pool.
   *
   * This method does not require authentication.
   *
   * @param url url to the group's page or photo pool. Required.
   * @return group NSID and name.
   * @throws JinxException if the url is missing, or if there are any errors.
   * @see <a href="https://www.flickr.com/services/api/flickr.urls.lookupGroup.html">flickr.urls.lookupGroup</a>
   */
  public GroupUrls lookupGroup(String url) throws JinxException {
    JinxUtils.validateParams(url);
    Map<String, String> params = new TreeMap<>();
    params.put("method", "flickr.urls.lookupGroup");
    params.put("url", url);
    return jinx.flickrGet(params, GroupUrls.class, false);
  }

  /**
   * Returns a user NSID, given the url to a user's photos or profile.
   *
   * This method does not require authentication.
   *
   * @param url url to the user's profile or photos page. Required.
   * @return user id and username.
   * @throws JinxException if there are any errors.
   * @see <a href="https://www.flickr.com/services/api/flickr.urls.lookupUser.html">flickr.urls.lookupUser</a>
   */
  public UserUrls lookupUser(String url) throws JinxException {
    JinxUtils.validateParams(url);
    Map<String, String> params = new TreeMap<>();
    params.put("method", "flickr.urls.lookupUser");
    params.put("url", url);
    return jinx.flickrGet(params, UserUrls.class);
  }
}