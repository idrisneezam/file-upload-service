package com.autodesk.file_upload_service;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * Test suite to run all tests for the file upload service
 */
@Suite
@SelectClasses({
    com.autodesk.file_upload_service.controller.FileUploadControllerTest.class,
    com.autodesk.file_upload_service.service.SimpleUnitTests.class,
    com.autodesk.file_upload_service.service.DatabaseServiceTest.class
})
public class TestRunner {
    // This class serves as a test suite runner
}
