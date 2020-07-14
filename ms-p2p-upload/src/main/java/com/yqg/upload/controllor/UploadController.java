package com.yqg.upload.controllor;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.yqg.api.upload.UploadServiceApi;
import com.yqg.api.upload.bo.UploadFileBo;
import com.yqg.api.upload.bo.UploadImgBo;
import com.yqg.api.upload.ro.DownloadContractRo;
import com.yqg.api.upload.ro.UploadBase64FileRo;
import com.yqg.api.upload.ro.UploadBase64ImgRo;
import com.yqg.api.upload.ro.UploadFileRo;
import com.yqg.api.upload.ro.UploadImgRo;
import com.yqg.common.core.BaseControllor;
import com.yqg.common.core.annocation.TruncateResultLog;
import com.yqg.common.core.response.BaseResponse;
import com.yqg.common.exceptions.BaseExceptionEnums;
import com.yqg.common.exceptions.BusinessException;
import com.yqg.common.utils.Base64Utils;
import com.yqg.upload.service.UploadService;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传
 * Created by gao on 2018/06/24.
 */
@RestController
public class UploadController extends BaseControllor {

    public Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    UploadService uploadService;


    /**
     * 文件上传
     *
     * @param file
     * @param uploadFileRo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {UploadServiceApi.path_file,UploadServiceApi.path_file_control}, method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse<UploadFileBo> uploadFile(@RequestParam("file") MultipartFile file,
                                                 UploadFileRo uploadFileRo) throws Exception {
        UploadFileBo uploadFileBo;
        if (file.isEmpty()) {

            throw new BusinessException(BaseExceptionEnums.UPLOAD_EMPTY_ERROR);

        } else {
            uploadFileBo = uploadService.fileSave(file, uploadFileRo);
        }

        return new BaseResponse<UploadFileBo>().successResponse(uploadFileBo);
    }

    @TruncateResultLog
    @RequestMapping(value = {UploadServiceApi.path_showImage,UploadServiceApi.path_showImageControl}, method = RequestMethod.GET,produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] showStreamOnBrowser(@RequestParam("path") String path) throws Exception {

        if (!path.startsWith("/mnt/MyUpload")) {
            return null;
        }
        InputStream stream = new FileInputStream(path);

        return IOUtils.toByteArray(stream);

    }
    
    @TruncateResultLog
    @RequestMapping(value = "/public/api-upload/downloadContract/{creditorNo}", method = RequestMethod.POST,produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<String> downloadContract(@PathVariable String creditorNo, @RequestBody DownloadContractRo downloadContractRo) throws Exception {

        byte[] stream = uploadService.downloadContract(creditorNo);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Disposition", 
          String.format("inline; filename=contract_%s", creditorNo));
        return ResponseEntity.ok().headers(responseHeaders).body(Base64Utils.encode(stream));
    }

    @TruncateResultLog
    @RequestMapping(value = {UploadServiceApi.path_downloadAttachment,UploadServiceApi.path_downloadAttachmentControl}, method = RequestMethod.GET,produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> downloadAttachment(@RequestParam("path") String path) throws Exception {

        if (!path.startsWith("/mnt/MyUpload")) {
            return null;
        }
        InputStream stream = new FileInputStream(path);
        String[] split = path.split("/");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Disposition", 
          String.format("attachment; filename=%s", split[split.length-1]));
     
        return ResponseEntity.ok()
          .headers(responseHeaders)
          .body(IOUtils.toByteArray(stream));

    }

    /**
     * 图片上传
     *
     * @param file
     * @param uploadImgRo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {UploadServiceApi.path_image,UploadServiceApi.path_image_control}, method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse<UploadImgBo> uploadImage(@RequestParam("file") MultipartFile file,
                                                 UploadImgRo uploadImgRo) throws Exception {
        UploadImgBo uploadImgBo = new UploadImgBo();
        logger.info("用户登录信息uploadImgBo：{}",uploadImgBo);
        if (file.isEmpty()) {

            throw new BusinessException(BaseExceptionEnums.UPLOAD_EMPTY_ERROR);

        } else {

            Image image = ImageIO.read(file.getInputStream());

            if (image == null) {
                throw new BusinessException(BaseExceptionEnums.UPLOAD_FILE_TYPE_ERROR);
            }
            UploadFileBo uploadFileBo = uploadService.fileSave(file, uploadImgRo);
            uploadImgBo.setFilePath(uploadFileBo.getFilePath());
            //是否生成缩略图
            if (uploadImgRo.isCreateThumbnail()) {
                uploadService.imgThumbnailSave(image, uploadImgBo);
            }
        }

        return new BaseResponse<UploadImgBo>().successResponse(uploadImgBo);
    }

    /**
     * base64文件上传
     * @param uploadBase64FileRo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = UploadServiceApi.path_base64File, method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse<UploadFileBo> uploadBase64File(UploadBase64FileRo uploadBase64FileRo) throws Exception {
        MultipartFile file = getMultipartFile(uploadBase64FileRo);
        return this.uploadFile(file, uploadBase64FileRo);
    }

    /**
     * base64图片上传
     * @param uploadBase64ImgRo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = UploadServiceApi.path_base64Image, method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse<UploadImgBo> uploadBase64Image(UploadBase64ImgRo uploadBase64ImgRo) throws Exception {
        MultipartFile file = getMultipartFile(uploadBase64ImgRo);
        UploadImgRo uploadImgRo = new UploadImgRo();
        uploadImgRo.setSessionId(uploadBase64ImgRo.getSessionId());
        uploadImgRo.setFileType(uploadBase64ImgRo.getFileType());
        uploadImgRo.setCreateThumbnail(uploadBase64ImgRo.isCreateThumbnail());
        return this.uploadImage(file, uploadImgRo);
    }

    /**
     * base64数据转文件
     * @param uploadBase64FileRo
     * @return
     */
    private MultipartFile getMultipartFile(final UploadBase64FileRo uploadBase64FileRo) {
        // Base64解码
        byte[] base64Data = org.apache.commons.codec.binary.Base64.decodeBase64(uploadBase64FileRo.getBase64Data());
        for (int i = 0; i < base64Data.length; ++i) {
            if (base64Data[i] < 0) {// 调整异常数据
                base64Data[i] += 256;
            }
        }
        return new MultipartFile() {
            @Override
            public String getName() {
                return "file";
            }

            @Override
            public String getOriginalFilename() {
                return uploadBase64FileRo.getFileName();
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return base64Data.length;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return base64Data;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(base64Data);
            }

            @Override
            public void transferTo(File file1) throws IOException, IllegalStateException {

            }
        };
    }


}
