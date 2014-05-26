package net.jeremybrooks.jinx.api;

import net.jeremybrooks.jinx.Jinx;
import net.jeremybrooks.jinx.JinxConstants;
import net.jeremybrooks.jinx.OAuthAccessToken;
import net.jeremybrooks.jinx.response.Response;
import net.jeremybrooks.jinx.response.common.Context;
import net.jeremybrooks.jinx.response.photosets.Photoset;
import net.jeremybrooks.jinx.response.photosets.PhotosetInfo;
import net.jeremybrooks.jinx.response.photosets.PhotosetList;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Properties;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNull;

public class PhotosetsApiTest {

	private static PhotosetsApi photosetsApi;
	private static PhotosetInfo photosetResponse;
	private static String createdPhotosetId;
	private static String photo0 = "14089985200";
	private static String photo1 = "14089938519";
	private static String photo2 = "14296772893";
	private static String photo3 = "14253479216";
	private static String photo4 = "14089987020";
	private static String photo5 = "14069360219";
	private static String photo6 = "14276190763";

	private static String photoset0 = "72157644903065293";
	private static String photoset1 = "72157644861154354";
	private static String photoset2 = "72157644807061466";


	@BeforeClass
	public static void beforeClass() throws Exception {
		Properties p = new Properties();
		p.load(OAuthApiTest.class.getResourceAsStream("/response/auth/secret.properties"));

		String filename = p.getProperty("path.to.oauth.token");
		assertNotNull(filename);

		File file = new File(filename);
		assertTrue(file.exists());

		OAuthAccessToken oAuthAccessToken = new OAuthAccessToken();
		oAuthAccessToken.load(new FileInputStream(file));

		assertNotNull(oAuthAccessToken);

		photosetsApi = new PhotosetsApi(new Jinx(p.getProperty("flickr.key"), p.getProperty("flickr.secret"), oAuthAccessToken));
	}

	@Test
	public void testCreate() throws Exception {
		String title = "Test Photoest";
		String description = "Created by jinx for testing.";
		photosetResponse = photosetsApi.create(title, description, photo0);
		assertNotNull(photosetResponse);
		Photoset p = photosetResponse.getPhotoset();
		assertNotNull(p);
		assertNotNull(p.getPhotosetId());
		createdPhotosetId = p.getPhotosetId();
		assertNotNull(p.getUrl());
	}

	@Test
	public void testAddPhoto() throws Exception {
		Response response = photosetsApi.addPhoto(createdPhotosetId, photo1);
		assertNotNull(response);
		assertEquals(0, response.getCode());
		assertEquals("ok", response.getStat());
	}


	@Test
	public void testGetContext() throws Exception {
		photosetsApi.addPhoto(createdPhotosetId, photo2);
		photosetsApi.addPhoto(createdPhotosetId, photo3);
		Context context = photosetsApi.getContext(photo1, createdPhotosetId);
		assertEquals(photo0, context.getPrevphoto().getPhotoId());
		assertEquals(photo2, context.getNextphoto().getPhotoId());
	}

	@Test
	public void testEditPhotos() throws Exception {
		List<String> photos = new ArrayList<String>();
		photos.add(photo4);
		photos.add(photo5);
		photos.add(photo6);
		photos.add(photo0);
		photos.add(photo1);
		photos.add(photo2);
		photos.add(photo3);
		photosetsApi.editPhotos(createdPhotosetId, photo5, photos);

		Context context = photosetsApi.getContext(photo5, createdPhotosetId);
		assertEquals(photo4, context.getPrevphoto().getPhotoId());
		assertEquals(photo6, context.getNextphoto().getPhotoId());
	}

	@Test
	public void testReorderPhotos() throws Exception {
		List<String> photos = new ArrayList<String>();
		photos.add(photo5);
		photos.add(photo3);
		photos.add(photo1);
		photos.add(photo6);
		photos.add(photo0);
		photos.add(photo4);
		photos.add(photo2);

		Response response = photosetsApi.reorderPhotos(createdPhotosetId, photos);
		assertNotNull(response);
		assertEquals(0, response.getCode());
		assertEquals("ok", response.getStat());
	}


	@Test
	public void testEditMeta() throws Exception {
		Response response = photosetsApi.editMeta(createdPhotosetId, "New Title", "New Description");
		assertNotNull(response);
		assertEquals(0, response.getCode());
		assertEquals("ok", response.getStat());
	}


	@Test
	public void testGetInfo() throws Exception {
		PhotosetInfo info = photosetsApi.getInfo(createdPhotosetId);
		assertNotNull(info);
		Photoset p = info.getPhotoset();
		assertNotNull(p);
		assertEquals(createdPhotosetId, p.getPhotosetId());
		assertEquals(photo5, p.getPrimary());
		assertEquals("New Title", p.getTitle());
	}

	@Test
	public void testSetPrimaryPhoto() throws Exception {
		Response response = photosetsApi.setPrimaryPhoto(createdPhotosetId, photo6);
		assertNotNull(response);
		assertEquals(0, response.getCode());
		assertEquals("ok", response.getStat());
		PhotosetInfo info = photosetsApi.getInfo(createdPhotosetId);
		assertEquals(photo6, info.getPhotoset().getPrimary());
	}

	@Test
	public void testRemovePhoto() throws Exception {
		Response response = photosetsApi.removePhoto(createdPhotosetId, photo6);
		assertNotNull(response);
		assertEquals(0, response.getCode());
		assertEquals("ok", response.getStat());
	}

	@Test
	public void testRemovePhotos() throws Exception {
		List<String> list = new ArrayList<String>();
		list.add(photo0);
		list.add(photo1);
		Response response = photosetsApi.removePhotos(createdPhotosetId, list);
		assertNotNull(response);
		assertEquals(0, response.getCode());
		assertEquals("ok", response.getStat());
	}


	@Test
	public void testDelete() throws Exception {
		Response response = photosetsApi.delete(createdPhotosetId);
		assertNotNull(response);
		assertEquals(0, response.getCode());
		assertEquals("ok", response.getStat());
	}

	@Test
	public void testGetList() throws Exception {
		PhotosetList list = photosetsApi.getList(null, 0, 0, null);
		assertNotNull(list);
		assertNotNull(list.getPhotosets());
		assertTrue(list.getPhotosets().getPhotosetList().size() > 0);
		assertNull(list.getPhotosets().getPhotosetList().get(0).getPrimaryPhotoExtras());
	}

	@Test
	public void testGetListPaged() throws Exception {
		PhotosetList list = photosetsApi.getList(null, 2, 5, null);
		assertNotNull(list);
		assertNotNull(list.getPhotosets());
		assertEquals(5, list.getPhotosets().getPhotosetList().size());
		assertNull(list.getPhotosets().getPhotosetList().get(0).getPrimaryPhotoExtras());
	}

	@Test
	public void testGetListWithAllExtras() throws Exception {
		EnumSet<JinxConstants.PhotoExtras> extras = EnumSet.allOf(JinxConstants.PhotoExtras.class);
		PhotosetList list = photosetsApi.getList(null, 2, 5, extras);
		assertNotNull(list.getPhotosets());
		assertEquals(5, list.getPhotosets().getPhotosetList().size());
		assertNotNull(list.getPhotosets().getPhotosetList().get(0).getPrimaryPhotoExtras());
	}

	@Test
	public void testGetListWithSomeExtras() throws Exception {
		EnumSet<JinxConstants.PhotoExtras> extras = EnumSet.of(JinxConstants.PhotoExtras.owner_name);
		PhotosetList list = photosetsApi.getList(null, 2, 5, extras);
		assertNotNull(list.getPhotosets());
		assertEquals(5, list.getPhotosets().getPhotosetList().size());
		assertNotNull(list.getPhotosets().getPhotosetList().get(0).getPrimaryPhotoExtras());
	}

	@Test
	public void testGetListFromUser() throws Exception {
		PhotosetList list = photosetsApi.getList("51035555243@N01", 2, 5, null);
		assertNotNull(list);
		assertNotNull(list.getPhotosets());
		assertEquals(5, list.getPhotosets().getPhotosetList().size());
		assertNull(list.getPhotosets().getPhotosetList().get(0).getPrimaryPhotoExtras());
	}

	@Test
	public void testOrderSets() throws Exception {
		List<String> list = new ArrayList<String>();
		list.add(photoset0);
		list.add(photoset1);
		list.add(photoset2);
		Response response = photosetsApi.orderSets(list);
		assertNotNull(response);
		assertEquals("ok", response.getStat());
		assertEquals(0, response.getCode());
	}


}