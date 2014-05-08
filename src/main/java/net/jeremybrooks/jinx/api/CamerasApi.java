/*
 * Jinx is Copyright 2010-2013 by Jeremy Brooks and Contributors
 *
 * This file is part of Jinx.
 *
 * Jinx is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Jinx is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Jinx.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.jeremybrooks.jinx.api;

import net.jeremybrooks.jinx.Jinx;
import net.jeremybrooks.jinx.JinxException;
import net.jeremybrooks.jinx.JinxUtils;
import net.jeremybrooks.jinx.response.CameraBrands;
import net.jeremybrooks.jinx.response.CameraModels;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Jeremy Brooks
 */
public class CamerasApi {

	private Jinx jinx;

	public CamerasApi(Jinx jinx) {
		this.jinx = jinx;
	}


	/**
	 * flickr.cameras.getBrands
	 * <p/>
	 * Returns all the brands of cameras that Flickr knows about.
	 * Authentication
	 * <p/>
	 * This method does not require authentication.
	 *
	 * @return
	 */
	public CameraBrands getBrands() throws JinxException {
		Map<String, String> params = new TreeMap<String, String>();
		params.put("method", "flickr.cameras.getBrands");

		return jinx.flickrGet(params, CameraBrands.class, false);
	}


	/**
	 * flickr.cameras.getBrandModels
	 * <p/>
	 * Retrieve all the models for a given camera brand.
	 * This method does not require authentication.
	 *
	 * @param brandId (Required) the ID of the requested brand (as returned from {@link net.jeremybrooks.jinx.api.CamerasApi#getBrands()}).
	 * @return
	 * @throws JinxException
	 */
	public CameraModels getBrandModels(String brandId) throws JinxException {
		JinxUtils.validateParams(brandId);
		Map<String, String> params = new TreeMap<String, String>();
		params.put("method", "flickr.cameras.getBrandModels");
		params.put("brand", brandId);

		return jinx.flickrGet(params, CameraModels.class, false);
	}
}