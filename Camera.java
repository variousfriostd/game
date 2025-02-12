package com.friostd.game;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

public class Camera {
	private final long window;
	private float zoom = 1.0f;
	public float cameraX = 0.0f, cameraY = 0.0f;
	private final float baseMoveSpeed = 0.005f; // Reduzido para menor sensibilidade
	private float moveX = 0.0f, moveY = 0.0f; // Direção de movimento

	public Camera(long window) {
		this.window = window;
	}

	public void setupCallbacks() {
		GLFW.glfwSetKeyCallback(window, new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				// Atualizar direções de movimento com base nas teclas pressionadas
				if (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT) {
					switch (key) {
						case GLFW.GLFW_KEY_W:
							moveY = baseMoveSpeed * zoom; // Movimento para cima, ajustado pelo zoom
							break;
						case GLFW.GLFW_KEY_S:
							moveY = -baseMoveSpeed * zoom; // Movimento para baixo, ajustado pelo zoom
							break;
						case GLFW.GLFW_KEY_A:
							moveX = -baseMoveSpeed * zoom; // Movimento para a esquerda, ajustado pelo zoom
							break;
						case GLFW.GLFW_KEY_D:
							moveX = baseMoveSpeed * zoom; // Movimento para a direita, ajustado pelo zoom
							break;
					}
				} else if (action == GLFW.GLFW_RELEASE) {
					// Parar o movimento quando a tecla é liberada
					if (key == GLFW.GLFW_KEY_W || key == GLFW.GLFW_KEY_S) {
						moveY = 0.0f;
					}
					if (key == GLFW.GLFW_KEY_A || key == GLFW.GLFW_KEY_D) {
						moveX = 0.0f;
					}
				}
			}
		});

		// Callback para o scroll do mouse
		GLFW.glfwSetScrollCallback(window, (window, xoffset, yoffset) -> {
			zoom *= (1.0f - (float) yoffset * 0.1f);
			zoom = Math.max(0.1f, zoom);
			updateProjection();
		});
	}

	public void processInput() {
		// Atualizar a posição da câmera com base nas direções de movimento
		cameraX += moveX;
		cameraY += moveY;
		updateProjection();
	}

	public void updateProjection() {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer width = stack.mallocInt(1);
			IntBuffer height = stack.mallocInt(1);
			GLFW.glfwGetFramebufferSize(window, width, height);
			float aspectRatio = (float) width.get(0) / height.get(0);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(-aspectRatio * zoom + cameraX, aspectRatio * zoom + cameraX, -zoom + cameraY, zoom + cameraY, -1.0f, 1.0f);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
		}
	}
}