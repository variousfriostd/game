package com.friostd.game;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class Game {
	private long window;
	private Camera camera;
	private Renderer renderer;
	private Icon icon;

	public void run() {
		init();
		loop();
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
	}

	private void init() {
		// Inicializar GLFW
		if (!GLFW.glfwInit()) {
			throw new IllegalStateException("Falha ao inicializar GLFW");
		}

		// Configurar janela para iniciar maximizada
		GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);

		// Criar a janela
		window = GLFW.glfwCreateWindow(800, 600, "Cubo 2D com LWJGL", 0, 0);
		if (window == 0) {
			throw new RuntimeException("Falha ao criar janela GLFW");
		}

		// Maximizar a janela
		GLFW.glfwMaximizeWindow(window);

		// Definir o contexto OpenGL para a janela
		GLFW.glfwMakeContextCurrent(window);
		GL.createCapabilities();

		camera = new Camera(window);
		renderer = new Renderer();
		icon = new Icon(window, "icon.png", 10, 10, 32, 32); // Posicionar o ícone no canto superior esquerdo

		// Configurar callbacks
		camera.setupCallbacks();
		GLFW.glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
			if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT && action == GLFW.GLFW_PRESS) {
				double[] xpos = new double[1];
				double[] ypos = new double[1];
				GLFW.glfwGetCursorPos(window, xpos, ypos);

				if (icon.isClicked(xpos[0], ypos[1])) {
					// Centralizar a câmera
					camera.cameraX = 0.0f;
					camera.cameraY = 0.0f;
					camera.updateProjection();
				}
			}
		});
	}

	private void loop() {
		// Configurar cor de fundo
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		// Obter tamanho inicial do framebuffer
		camera.updateProjection();

		// Loop da renderização
		while (!GLFW.glfwWindowShouldClose(window)) {
			// Limpar o buffer de cores
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

			// Processar input do usuário
			camera.processInput();

			// Renderizar o quadrado
			renderer.render();

			// Desenhar o ícone
			icon.draw();

			// Trocar o buffer de exibição
			GLFW.glfwSwapBuffers(window);

			// Processar eventos de janela
			GLFW.glfwPollEvents();
		}
	}

	public static void main(String[] args) {
		new Game().run();
	}
}