package com.chan.labelimg.mapper;

import com.chan.labelimg.pojo.Img;
import com.chan.labelimg.pojo.Mission;
import com.chan.labelimg.pojo.MissionClaimer;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author: chen zheng
 * @Date: 12/9/2021 5:15 PM
 */
@Mapper
public interface MissionMapper {
    /**
     * @param description mission description
     * @param fromID      from ID
     * @return int code
     */
    @Insert("insert into mission(description, fromID, state, toID) values (#{description}, #{fromID}, 0, -1)")
    public int insertMission(@Param("description") String description, @Param("fromID") int fromID);

    /**
     * @return mission ID
     */
    @Select("select last_insert_id();")
    public int getMissionID();

    /**
     * @param missionID missionID
     * @param chose     choose images' IDs
     * @return int code
     */
    @Update("<script> update img set missionID = #{missionID} where id in <foreach item='item' index='index' collection='chose' open='(' separator=',' close=')'> #{item} </foreach> </script>")
    public int updateImages(@Param("missionID") int missionID, @Param("chose") List<Integer> chose);

    @Select("select * from mission where id = #{ID}")
    public Mission getMissionByMissionID(@Param("ID") int missionID);

    /**
     * @param ID from ID
     * @return all his missions
     */
    @Select("select * from mission where fromID = #{ID}")
    public List<Mission> getMyMission(@Param("ID") int ID);

    @Select("select * from mission")
    public List<Mission> getAllMission();

    @Select("select * from img where missionID = #{ID}")
    public List<Img> getImgByMissionID(@Param("ID") int ID);

    @Update("update mission set toID = #{userID}, state = 1 where id = #{missionID}")
    public int claimMission(@Param("missionID") int missionID, @Param("userID") int userID);

    @Select("select * from mission where toID = #{ID}")
    public List<Mission> getClaimedMission(@Param("ID") int ID);
}
