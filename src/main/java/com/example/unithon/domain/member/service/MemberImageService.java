package com.example.unithon.domain.member.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.unithon.global.exception.CustomException;
import com.example.unithon.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberImageService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadProfileImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // 파일 확장자 검증
        String originalFilename = file.getOriginalFilename();
        if (!isValidImageFile(originalFilename)) {
            log.warn("유효하지 않은 이미지 파일: {}", originalFilename);
            throw new CustomException(ErrorCode.INVALID_FILE_FORMAT);
        }

        // 파일 크기 제한 (2MB)
        if (file.getSize() > 2 * 1024 * 1024) {
            log.warn("파일 크기 초과: {}MB", file.getSize() / 1024 / 1024);
            throw new CustomException(ErrorCode.FILE_SIZE_EXCEEDED);
        }

        try {
            // 고유한 파일명 생성 (프로필 폴더에 저장)
            String fileName = "profiles/" + UUID.randomUUID() + "_" + originalFilename;

            // 메타데이터 설정
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            // S3에 파일 업로드
            amazonS3.putObject(bucket, fileName, file.getInputStream(), metadata);

            // 업로드된 파일의 URL 반환
            String imageUrl = amazonS3.getUrl(bucket, fileName).toString();
            log.info("프로필 이미지 업로드 성공: {}", fileName);
            return imageUrl;

        } catch (IOException e) {
            log.error("프로필 이미지 업로드 실패: {}", e.getMessage());
            throw new CustomException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }
    }

    public void deleteProfileImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return;
        }

        try {
            // URL에서 S3 key 추출
            String fileName = extractFileNameFromUrl(imageUrl);

            amazonS3.deleteObject(bucket, fileName);
            log.info("프로필 이미지 삭제 완료: {}", fileName);

        } catch (Exception e) {
            log.error("프로필 이미지 삭제 실패: {}", e.getMessage());
            // 삭제 실패는 서비스 로직에 영향을 주지 않도록 예외를 던지지 않음
        }
    }

    private boolean isValidImageFile(String filename) {
        if (filename == null || !filename.contains(".")) {
            return false;
        }

        String extension = filename.toLowerCase()
                .substring(filename.lastIndexOf(".") + 1);

        return extension.equals("jpg") ||
                extension.equals("jpeg") ||
                extension.equals("png") ||
                extension.equals("gif") ||
                extension.equals("webp");
    }

    private String extractFileNameFromUrl(String imageUrl) {
        // S3 URL에서 버킷명 이후의 키 추출
        if (imageUrl.contains("amazonaws.com/")) {
            return imageUrl.substring(imageUrl.indexOf("amazonaws.com/") + 14);
        }
        // 백업: 단순히 파일명만 추출
        return "profiles/" + imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
    }
}