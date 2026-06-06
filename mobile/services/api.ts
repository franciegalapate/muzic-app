import axios from "axios";

// const BASE_URL = 'http://backend:8080';
const BASE_URL = "http://192.168.100.202:8080";

export interface Track {
  id: string;
  name: string;
  artist: string;
  albumName: string;
  albumImageUrl: string;
  previewUrl: string | null;
  spotifyUrl: string;
}

export interface PlaylistResponse {
  journalEntryId: number;
  mood: string;
  energyLevel: string;
  playlistName: string;
  playlistDescription: string;
  tracks: Track[];
}

export const generatePlaylist = async (
  content: string,
): Promise<PlaylistResponse> => {
  const response = await axios.post(`${BASE_URL}/api/playlist/generate`, {
    content,
  });
  return response.data;
};
