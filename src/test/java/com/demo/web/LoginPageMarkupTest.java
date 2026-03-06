package com.demo.web;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginPageMarkupTest {

    @Test
    public void loginPageUsesIndustrialMinimalStyle() throws Exception {
        String html = new String(
            Files.readAllBytes(Paths.get("src/main/webapp/index/login.html")),
            StandardCharsets.UTF_8
        );

        assertTrue(html.contains("奉天数智天然气安全管理平台"));
        assertTrue(html.contains("industrial-login"));
        assertTrue(html.contains("status-badge"));
        assertTrue(html.contains("JetBrains Mono"));
        assertFalse(html.contains("bg-circles"));
        assertFalse(html.contains("wave-bg"));
    }
}
