package com.friostd.game;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL30.*;

public class Icon {
	private final long window;
	private final int textureId;
	private final float x, y;
	private final float width, height;
	private final int vao, vbo;

	public Icon(long window, String imagePath, float x, float y, float width, float height) {
		this.window = window;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		// Carregar a imagem
		ByteBuffer image;
		int imgWidth, imgHeight;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer widthBuffer = stack.mallocInt(1);
			IntBuffer heightBuffer = stack.mallocInt(1);
			IntBuffer channelsBuffer = stack.mallocInt(1);

			image = STBImage.stbi_load(imagePath, widthBuffer, heightBuffer, channelsBuffer, 4);
			if (image == null) {
				throw new RuntimeException("Falha ao carregar imagem: " + STBImage.stbi_failure_reason());
			}
			imgWidth = widthBuffer.get();
			imgHeight = heightBuffer.get();
		}

		// Criar e configurar a textura
		textureId = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureId);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, imgWidth, imgHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
		glGenerateMipmap(GL_TEXTURE_2D);

		// Configurar o VAO e o VBO
		vao = glGenVertexArrays();
		vbo = glGenBuffers();
		glBindVertexArray(vao);

		float[] vertices = {
				x, y, 0.0f, 0.0f,
				x + width, y, 1.0f, 0.0f,
				x + width, y + height, 1.0f, 1.0f,
				x, y + height, 0.0f, 1.0f
		};

		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * 4, 0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * 4, 2 * 4);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}

	public void draw() {
		glBindVertexArray(vao);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textureId);
		glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
		glBindVertexArray(0);
	}

	public boolean isClicked(double mouseX, double mouseY) {
		// Verificar se o clique está dentro dos limites do ícone
		return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
	}
}