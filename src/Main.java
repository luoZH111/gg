import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;



/**
 * <p>类{@code Role}是角色类，包括英雄和随从，拥有属性：攻击力和生命力。
 * @author luoZH
 * @since 2018/10/7
 */
class Role
{
	int health,power;
	Role(int h,int p)
	{
		this.health=h;
		this.power=p;
	}
};
/**
 * <p>类{@code Player}是玩家类，拥有属性：角色列表
 * @author luoZH
 * @since 2018/10/7
 */
class Player
{
	List<Role> player_list;
	/**
	 * <p>创建玩家的角色链表，并并给玩家创建一只英雄
	 */
	int  number_of_kill;
	Player()
	{
		player_list = new ArrayList<Role>();
		Role hero=new Role(30,0);
		player_list.add(hero);
	}
	
};
/**
 * <p>类{@code Game}是游戏类。
 * <br>Game
 * <br>拥有属性：玩家。
 * <br>拥有方法：AddFollower，judge_death_of_attacker，judge_death_of_defender，CheckGame，Show。
 * @author luoZH
 * @since 2018/10/7
 */
class Game
{
	/**
	 * <p>玩家列表，拥有属性：角色链表
	 * @author luoZH
	 */
	Player[] player = new Player[2];
	/**
	 * <p>游戏是否结束的标志
	 */
	boolean game_over;
	/**
	 * <p>每局游戏都有两个玩家，创建并初始化两个玩家
	 */
	Game()
	{
		/*for (int i = 0; i < 2; i++)
		{
			P[i] = new Player();
			P[i].MonsterList.add(new Monster(0, 30));
		}*/
		for(int i=0;i<player.length;i++)
		{
			player[i]=new Player();			
		}
	}
	/**
	 * <p>增加随从
	 * @param player 召唤随从的玩家
	 * @param pos 随从召唤的位置
	 * @param power 随从攻击力
	 * @param health 随从生命力
	 */
	void AddFollower(int curplayer, int pos, int power, int health)
	{
		if(game_over) return;
		//P[i].MonsterList.add(p, new Monster(a, h));
		int pos_of_follower=pos;
		int power_of_follower=power;
		int health_of_follower=health;
		Role follower=new Role(health_of_follower,power_of_follower);
		player[curplayer].player_list.add(pos_of_follower,follower);
		System.out.printf("curplayer:%d len:%d\n",curplayer,player[curplayer].player_list.size());
	}
	/**
	 * <p>攻击
	 * <br>一只怪物对另一只怪物发起攻击，双方都要承受伤害。
	 * @param curplayer 攻击方的玩家
	 * @param attacker 攻击者
	 * @param defender 防守者
	 */
	void Attack(int curplayer, int attacker, int defender)
	{
		if(game_over) return;
		
		int otherplayer=1-curplayer;
		
		int health_of_attacker=player[curplayer].player_list.get(attacker).health;
		int health_of_defender=player[otherplayer].player_list.get(defender).health;
		int power_of_attacker=player[curplayer].player_list.get(attacker).power;
		int power_of_defender=player[otherplayer].player_list.get(defender).power;
		health_of_attacker-=power_of_defender;
		health_of_defender-=power_of_attacker;
		
		player[curplayer].player_list.get(attacker).health=health_of_attacker;
		player[otherplayer].player_list.get(defender).health=health_of_defender;
	
	
		
		judge_death_of_attacker(curplayer,health_of_attacker,attacker);
		judge_death_of_defender(otherplayer,health_of_defender,defender);
		
		
	}
	/**
	 * <p>玩家英雄等级更新，对应生命值升高
	 * <br>玩家英雄生命值加10，杀敌数减4
	 * @param curplayer 当前的玩家
	 */
	void Update(int curplayer)
	{
		int hero=0;
		player[curplayer].player_list.get(hero).health+=10;
		player[curplayer].number_of_kill-=4;
	}
	/**
	 * <p>判定攻击者是否死亡
	 * <br>当攻击者生命值为负时，从玩家随从列表移除。
	 * @param curplayer 攻击随从的玩家
	 * @param health_of_attacker 攻击者生命值
	 * @param attacker 攻击者
	 */
	void judge_death_of_attacker(int curplayer, int health_of_attacker,int attacker)
	{
		if(health_of_attacker<=0)
		{
			int otherplayer=1-curplayer;//
			player[otherplayer].number_of_kill++; //
			player[curplayer].player_list.remove(attacker);
		}
	}
	/**
	 * <p>判定防御者是否死亡
	 * <br>当防御者生命值为负时，从玩家随从列表移除。
	 * @param otherplayer 防御随从的玩家
	 * @param health_of_defender 防御者生命值
	 * @param defender 防御者
	 */
	void judge_death_of_defender(int otherplayer,int health_of_defender,int defender)
	{
		if(defender!=0&&health_of_defender<=0)
		{
			int curplayer=1-otherplayer;//
			player[curplayer].number_of_kill++;//
			player[otherplayer].player_list.remove(defender);
		}
		if(defender==0&&health_of_defender<=0)
		{
			game_over=true; 
		}
	}
	/**
	 * <p>检查胜利者
	 * <br>在所有操作结束后，判定本局游戏结果。
	 * @return 返回一个游戏结果
	 */
	int CheckGame()
	{
		int health_of_hero_of_play1=player[0].player_list.get(0).health;
		int health_of_hero_of_play2=player[1].player_list.get(0).health;
		if(health_of_hero_of_play1<=0)
			return -1;
		else if(health_of_hero_of_play2<=0)
			return 1;
		else
			return 0;
	}
	/**
	 * <p>展示游戏结果
	 * <br>在游戏结束后，输出游戏的结局与情况。
	 * @param result_of_game 游戏结果
	 */
	void Show(int result_of_game)
	{
		int health_of_hero_of_play1=player[0].player_list.get(0).health;
		int health_of_hero_of_play2=player[1].player_list.get(0).health;
		//output the winner after N actions		
		System.out.println(result_of_game);
		
		
		int number_of_followers_of_player1=player[0].player_list.size()-1;
		System.out.println(health_of_hero_of_play1);
		System.out.print(number_of_followers_of_player1+" ");
		for(int i=1;i<=number_of_followers_of_player1;i++)
		{
			int health_of_ith_follower_of_player1=player[0].player_list.get(i).health;
			System.out.print(health_of_ith_follower_of_player1+" ");
		}
		System.out.println();
		
		int number_of_followers_of_player2=player[1].player_list.size()-1;
		System.out.println(health_of_hero_of_play2);
		System.out.print(number_of_followers_of_player2+" ");
		for(int i=1;i<=number_of_followers_of_player2;i++)
		{
			int health_of_ith_follower_of_player2=player[1].player_list.get(i).health;
			System.out.print(health_of_ith_follower_of_player2+" ");
		}
		System.out.println();
	}
};
/**
 * <p>类{@code Main1}是主类
 * <br>负责接受数据的输入，并将值传输给Game
 * @author luoZH
 * @since 2018/10/7
 */
public class Main
{
	public static void main(String[] args)
	{
		Logger logger = Logger.getLogger(Main.class);
		logger.setLevel(Level.INFO);
		
		Game game = new Game();
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		sc.nextLine();
		int curplayer = 0;
		while((n--) != 0)
		{
			String operation = sc.next();
			
			if (operation.equals("summon"))
			{
				
				game.AddFollower(curplayer, sc.nextInt(), sc.nextInt(), sc.nextInt());
			}
			else if (operation.equals("attack"))
				game.Attack(curplayer, sc.nextInt(), sc.nextInt());
			else if(operation.equals("update"))
				game.Update(curplayer);
			else if(operation.equals("end"))
				curplayer = 1 - curplayer;
			else
				logger.warn("illegal imput!");
		}
		game.Show(game.CheckGame());
		sc.close();
	}
}