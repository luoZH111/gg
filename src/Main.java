import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;



/**
 * <p>��{@code Role}�ǽ�ɫ�࣬����Ӣ�ۺ���ӣ�ӵ�����ԣ�����������������
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
 * <p>��{@code Player}������࣬ӵ�����ԣ���ɫ�б�
 * @author luoZH
 * @since 2018/10/7
 */
class Player
{
	List<Role> player_list;
	/**
	 * <p>������ҵĽ�ɫ������������Ҵ���һֻӢ��
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
 * <p>��{@code Game}����Ϸ�ࡣ
 * <br>Game
 * <br>ӵ�����ԣ���ҡ�
 * <br>ӵ�з�����AddFollower��judge_death_of_attacker��judge_death_of_defender��CheckGame��Show��
 * @author luoZH
 * @since 2018/10/7
 */
class Game
{
	/**
	 * <p>����б�ӵ�����ԣ���ɫ����
	 * @author luoZH
	 */
	Player[] player = new Player[2];
	/**
	 * <p>��Ϸ�Ƿ�����ı�־
	 */
	boolean game_over;
	/**
	 * <p>ÿ����Ϸ����������ң���������ʼ���������
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
	 * <p>�������
	 * @param player �ٻ���ӵ����
	 * @param pos ����ٻ���λ��
	 * @param power ��ӹ�����
	 * @param health ���������
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
	 * <p>����
	 * <br>һֻ�������һֻ���﷢�𹥻���˫����Ҫ�����˺���
	 * @param curplayer �����������
	 * @param attacker ������
	 * @param defender ������
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
	 * <p>���Ӣ�۵ȼ����£���Ӧ����ֵ����
	 * <br>���Ӣ������ֵ��10��ɱ������4
	 * @param curplayer ��ǰ�����
	 */
	void Update(int curplayer)
	{
		int hero=0;
		player[curplayer].player_list.get(hero).health+=10;
		player[curplayer].number_of_kill-=4;
	}
	/**
	 * <p>�ж��������Ƿ�����
	 * <br>������������ֵΪ��ʱ�����������б��Ƴ���
	 * @param curplayer ������ӵ����
	 * @param health_of_attacker ����������ֵ
	 * @param attacker ������
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
	 * <p>�ж��������Ƿ�����
	 * <br>������������ֵΪ��ʱ�����������б��Ƴ���
	 * @param otherplayer ������ӵ����
	 * @param health_of_defender ����������ֵ
	 * @param defender ������
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
	 * <p>���ʤ����
	 * <br>�����в����������ж�������Ϸ�����
	 * @return ����һ����Ϸ���
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
	 * <p>չʾ��Ϸ���
	 * <br>����Ϸ�����������Ϸ�Ľ���������
	 * @param result_of_game ��Ϸ���
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
 * <p>��{@code Main1}������
 * <br>����������ݵ����룬����ֵ�����Game
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