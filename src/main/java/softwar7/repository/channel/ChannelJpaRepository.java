package softwar7.repository.channel;

import org.springframework.data.jpa.repository.JpaRepository;
import softwar7.domain.channel.Channel;

public interface ChannelJpaRepository extends JpaRepository<Channel, Long> {
}
