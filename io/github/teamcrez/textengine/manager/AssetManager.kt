package io.github.cheesesand.textengine.manager

import java.io.File

object AssetManager {

    fun checkAssetDir(path: String) {
        val assetFolder = File("assets")

        if (!assetFolder.exists()) {
            assetFolder.mkdir()
        }

        val targetAssetFolder = File("assets/$path")
        if (!targetAssetFolder.exists()) {
            targetAssetFolder.mkdirs()
        }
    }

}