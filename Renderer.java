package com.friostd.game;

import org.lwjgl.opengl.GL11;

public class Renderer {
	public void setupCallbacks(long window) {
		// Nenhum callback necessário para renderização no momento
	}

	public void render() {
		// Desenhar o quadrado
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor3f(1.0f, 0.0f, 0.0f); // Cor vermelha
		GL11.glVertex2f(-0.5f, 0.5f); // Vértice superior esquerdo
		GL11.glVertex2f(0.5f, 0.5f);  // Vértice superior direito
		GL11.glVertex2f(0.5f, -0.5f); // Vértice inferior direito
		GL11.glVertex2f(-0.5f, -0.5f);// Vértice inferior esquerdo
		GL11.glEnd();
	}
}