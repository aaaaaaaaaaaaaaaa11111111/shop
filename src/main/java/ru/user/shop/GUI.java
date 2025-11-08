package ru.user.shop;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;

public class GUI extends JFrame {
	public GUI() {
		super("Shop");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		setLayout(new GridLayout(10, 10));
		setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());

		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.gridx = 0;
		gbc1.gridy = 0;
		JComboBox<String> b1 = new JComboBox<String>();
		b1.addItem("Вывести");
		b1.addItem("Искать");
		b1.addItem("Вставить");
		add(b1, gbc1);

		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.gridx = 0;
		gbc2.gridy = 1;
		JComboBox<String> b2 = new JComboBox<String>();
		b2.addItem("Покупатель");
		b2.addItem("Сотрудник");
		b2.addItem("Магазин");
		add(b2, gbc2);

		GridBagConstraints gbc3 = new GridBagConstraints();
		gbc3.gridx = 0;
		gbc3.gridy = 1;
		add(new JButton("Выполнить"), gbc3);

		setVisible(true);
	}
}
